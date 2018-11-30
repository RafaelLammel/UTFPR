var connectionFactory = require('../ConnectionFactory');
var conn = connectionFactory();

module.exports = {
    async store(req, res){
        conn.query('INSERT INTO cartaodecredito (nome, numero, validade, numeroDeSeguranca, ID) VALUES (?,?,?,?,?)',
        [req.body.nome, req.body.numero, req.body.validade, req.body.numeroDeSeguranca,req.body.ID]
        , function (error, results, fields) {
          if (error) throw error;
          return res.json(results);
      });
    },
};
