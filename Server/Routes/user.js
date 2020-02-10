const express = require("express");
const router = express.Router();
// Load User model
const User = require("../Models/User");
// Import the the BcryptJS Library -> BcryptJS is a no setup encryption tool
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const nodemailer = require('nodemailer');
require('dotenv').config();


// import secret
const secret = process.env.SECRET ;
// import email password
const emailPassword = process.env.EMAIL_PASS ;

// Read all entries
router.get('/all', (req, res) => {
 
  User.find()
    .sort({ date: -1 })
    
    .then(items => {
      res.json(items)
      console.log("Success")
    })
});

// Register new user Route
router.post('/register', (req, res) => {
  User.findOne({ emailAddress: req.body.emailAddress})
    .then(user => {
      console.log("us: "+ user);
    if (user) {
      let error = 'Email Address Exists in Database.';
      return res.status(400).json(error);
    } else{
      const newUser = new User({
        name: req.body.name,
        emailAddress: req.body.emailAddress,
        password: req.body.password,
        userName: req.body.userName
      });
      bcrypt.genSalt(10, (err, salt) => {
        if (err) throw err;
        bcrypt.hash(newUser.password, salt,
          (err, hash) => {
            if (err) throw err;
            newUser.password = hash;
            newUser.save().then(user => res.json(user))
              .catch(err => res.status(400).json(err));
          });
      });
    }
  });
});

// Forget password of user Route
router.post('/forgetPassword', (req, res) => {
  User.findOne({ emailAddress: req.body.emailAddress})
    .then(user => {
      console.log("us: "+ user);
      
    if (!user) {
      error.email = "No Account Found";
      return res.status(404).json(error);
    } else{
      let result = false;
      try {
        result = send_mail(user.emailAddress,  user.password)
      } catch (error) {
        res.json({
          success: false,
        });
      }
      
      if (result){
        res.json({
          success: true,
        });
      }else{
        res.status(500).json({
          error: "Error sending email",
          raw: err
        });
      }
    }
  });
});

//  Login Route
router.post('/login', (req, res) => {
  const password = req.body.password;
  let error = {};
  User.findOne({ emailAddress: req.body.emailAddress })
    .then(user => {
      console.log(user)
      if (!user) {
        error.email = "No Account Found";
        return res.status(404).json(error);
      }
      bcrypt.compare(password, user.password)
        .then(isMatch => {
          if (isMatch) {
            const payload = {
              id: user._id,
              name: user.userName
            };
            jwt.sign(payload, secret, { expiresIn: 36000 },
              (err, token) => {
                if (err) {
                  res.status(500).json({
                    error: "Error signing token",
                    raw: err
                  });
                } else{
                  res.json({
                    success: true,
                    token: `Bearer ${token}`,
                    id: user._id,
                    userName: user.userName
                  });
                }
              });
          } else {
            error.password = "Password is incorrect";
            res.status(400).json(error);
          }
        })
        .catch(err => {
          console.log(err)
        })
    })
    .catch(err =>{
      console.log(err)
    })
});

function send_mail(email, password){
  console.log(password)
  var transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
      user: 'mhealthcare83@gmail.com',
      pass: 'MediCare7568'
    }
  });
  
  var mailOptions = {
    from: 'mhealthcare83@gmail.com',
    to: email,
    subject: 'Sending Email using Node.js',
    text: 'That was easy! Your Password is: ' + password
  };
  
  transporter.sendMail(mailOptions, function(error, info){
    if (error) {
      console.log(error);
      return false
    } else {
      console.log('Email sent: ' + info.response);
      return true
    }
  });
}




module.exports = router;
