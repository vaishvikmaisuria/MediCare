const express = require("express");
const mongoose = require("mongoose");
const bodyparser = require("body-parser");
const users = require("./Routes/user");
const userProfile = require("./Routes/Profile");

const cors = require("cors");
require('dotenv').config(); 
const cookieparser = require('cookie-parser');
const passport = require('passport');
const initializePassport = require('./passport_config');




// import key
const db = process.env.DB_URI ;
// create an express app
const app = express();
// define Port
const port = process.env.PORT || 8000;

// Middlewares
app.use(cookieparser());
app.use(bodyparser.json({ limit: "50kb" }))
app.use(bodyparser.urlencoded({ extended: true, limit: "50kb"}));

// connect to mongo server
mongoose
  .connect(db, { useNewUrlParser: true, useCreateIndex: true, useFindAndModify: false, useUnifiedTopology: true })
  .then(() => console.log("MongoDb server connected..."))
  .catch(err => console.log(err));

//initializes the passport configuration. imports our configuration file which holds our verification callbacks and things like the secret for signing.
app.use(passport.initialize());
initializePassport(passport);

//custom Middleware for logging the each request going to the API
app.use((req,res,next) => {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept");
  res.header("Access-Control-Allow-Methods",
    "GET, POST, PUT, DELETE, OPTIONS");
  if (req.body) console.log(req.body);
  if (req.params) console.log(req.params);
  if(req.query) console.log(req.query);
  if(req) console.log(req.get("Authorization"));
  console.log(`Received a ${req.method} request from ${req.ip} for ${req.url}`);
  next();
});

// routes
app.use(cors());
app.use("/users", users);
app.use("/profile", passport.authenticate('jwt', {session:false}), userProfile);




// Testing
app.get('/', (req, res) => {
  res.send('Hello World!')
});

app.listen(port, () => {
  console.log(`Listening for Requests on port: ${port}`);
});