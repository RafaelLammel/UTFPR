var connectionFactory = require('../ConnectionFactory');
var conn = connectionFactory();

module.exports = {
    async store(req, res){
        conn.query('INSERT INTO contabancaria (conta, agencia, ID) VALUES (?,?,?)',
        [req.body.conta, req.body.agencia, req.body.ID]
        , function (error, results, fields) {
          if (error) throw error;
          return res.json(results);
      });
    },
};
