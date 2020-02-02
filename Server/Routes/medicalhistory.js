const express = require("express");
const router = express.Router();
// Load User model
const User = require("../Models/User");
// Import the the BcryptJS Library -> BcryptJS is a no setup encryption tool
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');



// get all medical history
router.get('/all', (req, res) => {
 
  User.find()
    .sort({ date: -1 })
    
    .then(items => {
      res.json(items)
      console.log("Success")
    })
});


// new User Medical History 
router.post('/mymediHistory', (req, res) => {
  User.findOne({ emailAddress: req.body.emailAddress})
    .then(user => {
      console.log("us: "+ user);
    if (user) {
      
      
    } else{
        let error = 'Medical History Does Exists in Database.';
        return res.status(400).json(error);
    }
  });
});




module.exports = router;
