var express = require('express');
var router = express.Router();
var nodemailer = require('nodemailer');
require('dotenv').config();
var cors = require('cors');
const creds = require('./config');
var app= express();
app.use(express.json());
const port= 4000;

  app.use((request, response, next) => {
    response.header("Access-Control-Allow-Origin", "*");
    response.header("Access-Control-Allow-Headers", "Content-Type");
    next();
  });

  const transporter = nodemailer.createTransport({
    service: "gmail", 
    //port: 2525,
    auth: {
      type: "OAuth2",
      user: "oerinspanish@gmail.com",
      pass: "usc12345",
      clientId: "1028717496461-hes8rncc89tr35ohfirgehqe6kgre87e.apps.googleusercontent.com",
      clientSecret: "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",
      accessToken: "ya29.a0ARrdaM8Pc0k-Un62s1qiMuUoEXJ3f56VTLVXW18bXthnHv1sMifQA3-LXRGOZQ8KY6cwsuYtvFh3hE50du5tD79lObizhFysX8gFy3cytD2r_SXprtS9g7urtLU6HsYVIfXdYeNQdu-eoapX6gF_eS6DRcT2",
      refreshToken: "4/0AX4XfWjtGOHZ6BBWEvU8c0PzkQV4SY81QV2TGqJJiPSq-fdk5Qw8A_25YdV8fNjqIkQmlQ",
    }
  });

  transporter.verify(function(error, success) {
    if (error) {
      console.log(error);
    } else {
      console.log("Server is ready to take our messages");
    }
  });

  app.post('/contact_us', (req, res, next) => {
    var name = req.body.name
    var email = req.body.email
    var subject = req.body.subject
    var message = req.body.message
  
    var mail = {
      from: email,
      to: "oerin.spanish@usc.edu",//my regular email I used to sign up to mailtrap
      subject: subject,
      text: message
    }
  
    transporter.sendMail(mail, (err, data) => {
      if (err) {
        console.log('failed', err);
        res.json({
          status: 'fail'
        })
      } else {
        console.log('successful');
        res.json({
         status: 'success'
        })
      }
    })
  })

  app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
  })


