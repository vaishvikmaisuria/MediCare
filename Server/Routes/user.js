const express = require("express");
const router = express.Router();
// Load User model
const User = require("../Models/User");
// Import the the BcryptJS Library -> BcryptJS is a no setup encryption tool
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

// import secret
const secret = process.env.SECRET;

/*  Manually add new user */
const newUser = new User({
  name: "Vash",
  emailAddress: "vash23@gmail.com",
  userName: "Vash7568",
  password: "pass1234"
})
// newUser
//   .save()
//   .then(item => console.log(item))
//   .catch(err => console.log(err));

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
                    token: `Bearer ${token}`
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

module.exports = router;
