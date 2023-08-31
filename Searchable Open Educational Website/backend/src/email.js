const AWS = require("aws-sdk");

// environment variables
const {REGION} = process.env;
// const REGION = "us-west-2"
AWS.config.update({region: REGION});
const ses = new AWS.SES({
  apiVersion: "2010-12-01",
  region: REGION,
});
const nodemailer = require('nodemailer');
const transporter = nodemailer.createTransport({
  SES: ses
});

const jsonResponse = (statusCode, body, additionalHeaders) => ({
  statusCode,
  body: JSON.stringify(body),
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    ...additionalHeaders,
  },
});

// transporter.verify(function(error, success) {
//   if (error) {
//     console.log("Error verifying", error);
//   } else {
//     console.log("Server is ready to take our messages");
//   }
// });

// from https://stackoverflow.com/a/55895889/8170714
const syncSendMail = mailOptions => new Promise(function (resolve, reject) {
  transporter.sendMail(mailOptions, (err, info) => {
    if (err) {
      console.error("Error: ", err);
      reject(err);
    } else {
      console.log(`Mail sent successfully!`);
      resolve(info);
    }
  });
})

exports.contact = async (event, context) => {
  console.log('Contact request received:', event);
  const body = JSON.parse(event.body);
  // const body = {
  //   name: "Test",
  //   email: "from@email.com",
  //   subject: "Subject",
  //   message: "Message"
  // }

  const name = body.name;
  const email = body.email;
  const subject = body.subject;
  const message = body.message;

  const mail = {
    from: {name: name, address: "contact@oerinspanish.org"},
    replyTo: email,
    to: "oerin.spanish@usc.edu",
    subject: `[Contact Form] ${subject}`,
    text: `Name: ${name}\nEmail: ${email}\nMessage:\n${message}`
  };

  try {
    await syncSendMail(mail);
    return jsonResponse(200, {status: 'success'})
  } catch (e) {
    return jsonResponse(500, {status: 'fail'})
  }
}

// contact("", "").then(out => console.log(out))


