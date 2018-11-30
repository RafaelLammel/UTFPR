var connectionFactory = require('../ConnectionFactory');
var conn = connectionFactory();

module.exports = {
    async getFinancial(req, res){
        conn.query('SELECT * FROM contaFinanceira WHERE idUsuario=?',[req.params.id],function (error, results, fields) {
          if (error) throw error;
          return res.json(results);
      });
    },
    async store(req, res){
        conn.query('INSERT INTO contafinanceira (idUsuario) VALUES (?)',
        [req.body.id]
        , function (error, results, fields) {
          if (error) throw error;
          return res.json(results);
      });
  },
};
