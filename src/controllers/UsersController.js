var connectionFactory = require('../ConnectionFactory');
var conn = connectionFactory();

module.exports = {
    async index(req, res){
        conn.query('SELECT * FROM usuario', function (error, results, fields) {
          if (error) throw error;
          return res.json(results);
      });
    },

    async login(req, res){
        conn.query('SELECT * FROM usuario WHERE email=?',[req.params.email],function (error, results, fields) {
          if (error) throw error;
          return res.json(results);
      });
    },

    async store(req, res){
        conn.query('INSERT INTO usuario (nome, email, senha, musico) VALUES (?,?,?,?)',
        [req.body.nome, req.body.email, req.body.senha, req.body.musico]
        , function (error, results, fields) {
          if (error) throw error;
          return res.json(results);
      });
  },

    async update(req, res){
        conn.query('UPDATE usuario SET ? WHERE id=?',[req.body,req.params.id],function (error, results, fields) {
          if (error) throw error;
          return res.json(results);
      });
    }
};
