const express = require('express');
const app = express();

app.use(express.json());

// App route to return server IP
app.get('/ipAddress', (req, res) => {
    // Static IP of VM, will not change
    const ip = "20.63.36.199";
    res.send({data: ip});
});

// Server time as a a string
app.get('/time', (req, res) => {
    const time = new Date().toLocaleTimeString("en-UK");
    res.send({data : time});
});

app.get('/name', (req, res) => {
    const name = 'Ariel Vetshchaizer';
    res.send({data: name});
});

const server = app.listen(8081, () => {
    console.log('Server is running on port http://%s:%s', server.address().address, server.address().port);
});
