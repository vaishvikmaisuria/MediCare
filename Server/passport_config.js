const User = require("./Models/User");
const mongoose = require("mongoose");
const { Strategy, ExtractJwt } = require('passport-jwt');


// import secret


const secret = process.env.SECRET; 

//this sets how we handle tokens coming from the requests that come
// and also defines the key to be used when verifying the token.
const opts = {
  jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
  secretOrKey: secret
};


module.exports = passport => {
  passport.use(
    new Strategy(opts, (jwt_payload, done) => {
      User.findById(jwt_payload.data._id)
      .then(user => {
        if (user) {
          return done(null, {
            id: user.id,
            name: user.name,
            email: user.email,
          });
        }else{
          return done(null, false);
        }
        
      })
      .catch(err => console.error(err));
    })
  )
}