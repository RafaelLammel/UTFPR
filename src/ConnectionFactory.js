var mysql = require('mysql');

module.exports = function() {
    var conn = mysql.createConnection({
        host: 'localhost',
        user: 'root',
        password: 'root',
        database: 'streetmusicdb'
    });
    return conn;
};
