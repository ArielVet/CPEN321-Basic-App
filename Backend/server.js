const express = require('express');
const app = express();
const db = require('./Database/dbSetup.js');

app.use(express.json());

// App route to return server IP
app.get('/ipAddress', (req, res) => {
    const ip = 0;
    res.send(ip);
});

// Server time as a a string
app.get('/time', (req, res) => {
    // Chose one or other
    const time = new Date().toLocaleTimeString();
    // const time = new Date().toString();

    res.send(time);
});

app.get('/name', (req, res) => {
    const name = 'Ariel Vetshchaizer';
    res.send(name);
});

const server = app.listen(8081, () => {
    console.log('Server is running on port http://%s:%s', server.address().address, server.address().port);
    db.connect();

});
