const express = require("express");
const router = express.Router();
// Load User model
const User = require("../Models/User");

// Read all entries
router.get('/all', (req, res) => {
 
    User.find()
      .sort({ date: -1 })
      
      .then(items => {
        res.json(items)
        // console.log("Success")
      })
  });


// Save user Profile picture path
router.post('/profilePicture', (req, res) => {
    User.updateOne({ "_id": req.body.id}, {"path": req.body.path})
      .then(user => {
      if (!user) {
        error.email = "No Account Found";
        return res.status(404).json(error);
      }
      return res.status(200).json("Success");
  
    });
});


// Save user Profile picture path
router.post('/getProfilePicture', (req, res) => {
    let error = {};
    User.findOne({ "_id": req.body.id })
        .then(user => {
            // console.log(user)
            if (!user) {
                error.email = "No Account Found";
                return res.status(404).json(error);
            }
            res.json({
                success: true,
                path: user.path
            });
        })
        .catch(err => {
            console.log(err)
        });
});


// Save user Location 
router.post('/setuserLocation', (req, res) => {
  User.updateOne({ "_id": req.body.id}, {"latitude": req.body.latitude})
    .then(user => {
      if (!user) {
        error.email = "No Account Found";
        return res.status(404).json(error);
      }
    })
    .catch(err => {
      console.log(err)
    });
  User.updateOne({ "_id": req.body.id}, {"longitude": req.body.longitude})
    .then(user => {
      if (!user) {
        error.email = "No Account Found";
        return res.status(404).json(error);
      }
    })
    .catch(err => {
      console.log(err)
    });
    return res.status(200).json("Success");
});

// Get user Location 
router.post('/getuserLocation', (req, res) => {
  let error = {};
  User.findOne({ "_id": req.body.id })
      .then(user => {
          // console.log(user)
          if (!user) {
              error.email = "No Account Found";
              return res.status(404).json(error);
          }
          res.json({
              success: true,
              latitude: user.latitude,
              longitude: user.longitude
          });
      })
      .catch(err => {
          console.log(err)
      });
});
  

// new User Medical History 
router.post('/mymediHistory', (req, res) => {
    User.findOne({ emailAddress: req.body.emailAddress})
      .then(user => {
        // console.log("us: "+ user);
      if (user) {
        
        
      } else{
          let error = 'Medical History Does Exists in Database.';
          return res.status(400).json(error);
      }
    });
  });


module.exports = router;