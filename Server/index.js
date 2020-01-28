const express = require("express");
const mongoose = require("mongoose");
const bodyparser = require("body-parser");
const sos = require("./Routes/Sos");
const users = require("./Routes/user");





// import key
const db = require("./Config/keys").mongoURI;

// create an express app
const app = express();

// Middlewares
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({
	extended: true
  }));


// connect to mongo server
mongoose
  .connect(db)
  .then(() => console.log("MongoDb server connected..."))
  .catch(err => console.log(err));

// routes
app.use("/user", users);


app.get('/', (req, res) => {
  res.send('Hello World!')
});

app.listen(8000, () => {
  console.log('Example app listening on port 8000!')
});