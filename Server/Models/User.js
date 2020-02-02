const mongoose = require("mongoose");
// use bcrypt to hash our password
var bcrypt = require('bcryptjs');
const jwt = require("jsonwebtoken");
const crypto = require("crypto");
const Schema = mongoose.Schema


const UserSchema = new Schema({
  name: String,
  emailAddress:{
      type: String,
      required: true,
      unique: true,
  },
  userName: {
      type: String,
      required: true,
      unique: true
  },
  password: {
      type: String,
      required: true
  }
});

const User = module.exports = mongoose.model("User", UserSchema);
