import cv2
import numpy as np
import statistics


PATH = "."
PROCESS_PATH = "./Processadas"
# Alterar para nome da imagem
IMG = "img.jpg"

CANNY_THRESHOLD1 = 200
CANNY_THRESHOLD2 = 250

HOUGH_RHO = 1
HOUGH_THETA = np.pi/360
HOUGH_THRESHOLD = 200

FINAL_LENGTH = 674
FINAL_HEIGHT = 467
FINAL_FILE_TYPE = "jpg"

def main():
    # Abrindo a imagem e mantendo uma cópia cinza
    img = cv2.imread(f"{PATH}/{IMG}")
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    cv2.imwrite(f"{PROCESS_PATH}/gray.{FINAL_FILE_TYPE}", gray)

    # Borrando a imagem e aplicando Canny para um versão com bordas da imagem
    blur = cv2.GaussianBlur(gray, (5, 5), 0)
    cv2.imwrite(f"{PROCESS_PATH}/blur.{FINAL_FILE_TYPE}", blur)
    _, binary = cv2.threshold(blur, 0, 255, cv2.THRESH_BINARY+cv2.THRESH_OTSU)
    cv2.imwrite(f"{PROCESS_PATH}/binay.{FINAL_FILE_TYPE}", binary)
    kernel = np.ones((5,5), np.uint8)
    closing = cv2.morphologyEx(binary, cv2.MORPH_CLOSE, kernel, iterations=5)
    cv2.imwrite(f"{PROCESS_PATH}/closing.{FINAL_FILE_TYPE}", closing)
    edges = cv2.Canny(closing, CANNY_THRESHOLD1, CANNY_THRESHOLD2)
    cv2.imwrite(f"{PROCESS_PATH}/edges.{FINAL_FILE_TYPE}", edges)

    # Pegando as linhas da transformada de Hough
    lines = cv2.HoughLines(edges, HOUGH_RHO, HOUGH_THETA, HOUGH_THRESHOLD)
    img_lines = np.copy(img)
    verticals = []
    horizontals = []
    for line in lines:
        for rho, theta in line:
            a = np.cos(theta)
            b = np.sin(theta)
            x0 = a*rho
            y0 = b*rho
            x1 = int(x0 + 2000*(-b))
            y1 = int(y0 + 2000*(a))
            x2 = int(x0 - 2000*(-b))
            y2 = int(y0 - 2000*(a))
            cv2.line(img_lines, (x1, y1), (x2, y2), (0, 0, 255))

            # Meio horizontal:
            if theta < np.pi/4 or (theta > 3*np.pi/4 and theta < 5*np.pi/4) or theta > 7*np.pi/4:
                horizontals.append(rho / np.sin(theta))
            else:
                verticals.append(rho / np.cos(theta))
    cv2.imwrite(f"{PROCESS_PATH}/lines.{FINAL_FILE_TYPE}", img_lines)

    # Deixando apenas 4 linhas
    horizontal_mean = np.mean(horizontals)
    vertical_mean = np.mean(verticals)

    left = []
    right = []
    up = []
    down = []
    for line in lines:
        for rho, theta in line:
            if theta < np.pi/4 or (theta > 3*np.pi/4 and theta < 5*np.pi/4) or theta > 7*np.pi/4:
                if rho / np.sin(theta) > horizontal_mean:
                    right.append(rho / np.sin(theta))
                else:
                    left.append(rho / np.sin(theta))
            else:
                if rho / np.cos(theta) > vertical_mean:
                    down.append(rho / np.cos(theta))
                else:
                    up.append(rho / np.cos(theta))
    
    up_median = statistics.mode(up)
    down_median = statistics.mode(down)
    left_median = statistics.mode(left)
    right_median = statistics.mode(right)
    
    paper_border = np.copy(img)
    upper = { "rho": 0, "theta": 0 }
    right = { "rho": 0, "theta": 0 }
    lower = { "rho": 0, "theta": 0 }
    left = { "rho": 0, "theta": 0 }
    for line in lines:
        for rho, theta in line:
            if rho / np.cos(theta) == down_median or rho / np.cos(theta) == up_median or rho / np.sin(theta) == left_median or rho / np.sin(theta) == right_median:
                if rho / np.cos(theta) == down_median:
                    lower["rho"] = rho
                    lower["theta"] = theta
                elif rho / np.cos(theta) == up_median:
                    upper["rho"] = rho
                    upper["theta"] = theta
                elif rho / np.sin(theta) == left_median:
                    left["rho"] = rho
                    left["theta"] = theta
                elif rho / np.sin(theta) == right_median:
                    right["rho"] = rho
                    right["theta"] = theta
                a = np.cos(theta)
                b = np.sin(theta)
                x0 = a*rho
                y0 = b*rho
                x1 = int(x0 + 2000*(-b))
                y1 = int(y0 + 2000*(a))
                x2 = int(x0 - 2000*(-b))
                y2 = int(y0 - 2000*(a))
                cv2.line(paper_border, (x1, y1), (x2, y2), (0, 0, 255))
    cv2.imwrite(f"{PROCESS_PATH}/PaperBorder.{FINAL_FILE_TYPE}", paper_border)
    
    upper_left_point = tuple([int(i) for i in np.linalg.solve(np.array([[np.cos(upper["theta"]), np.sin(upper["theta"])], [np.cos(left["theta"]), np.sin(left["theta"])]]), np.array([upper["rho"], left["rho"]]))])
    upper_right_point = tuple([int(i) for i in np.linalg.solve(np.array([[np.cos(upper["theta"]), np.sin(upper["theta"])], [np.cos(right["theta"]), np.sin(right["theta"])]]), np.array([upper["rho"], right["rho"]]))])
    lower_left_point = tuple([int(i) for i in np.linalg.solve(np.array([[np.cos(lower["theta"]), np.sin(lower["theta"])], [np.cos(left["theta"]), np.sin(left["theta"])]]), np.array([lower["rho"], left["rho"]]))])
    lower_right_point = tuple([int(i) for i in np.linalg.solve(np.array([[np.cos(lower["theta"]), np.sin(lower["theta"])], [np.cos(right["theta"]), np.sin(right["theta"])]]), np.array([lower["rho"], right["rho"]]))])

    points = np.copy(img)
    cv2.circle(points, upper_left_point, 30, (0,0,255), 10)
    cv2.circle(points, upper_right_point, 30, (0,0,255), 10)
    cv2.circle(points, lower_left_point, 30, (0,0,255), 10)
    cv2.circle(points, lower_right_point, 30, (0,0,255), 10)
    cv2.imwrite(f"{PROCESS_PATH}/Pontos.{FINAL_FILE_TYPE}", points)

    p1 = np.float32([list(upper_right_point), list(upper_left_point), list(lower_right_point), list(lower_left_point)])
    p2 = np.float32([[0,0], [FINAL_LENGTH, 0], [0, FINAL_HEIGHT], [FINAL_LENGTH, FINAL_HEIGHT]])
    mat = cv2.getPerspectiveTransform(p1, p2)
    out_img = cv2.warpPerspective(img, mat, (FINAL_LENGTH, FINAL_HEIGHT))
    cv2.imwrite(f"{PROCESS_PATH}/Final.{FINAL_FILE_TYPE}", out_img)


if __name__ == "__main__":
    main()