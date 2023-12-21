import express from 'express';
import path from 'path';
import { dirname } from 'path';
import fs from 'fs';
import { fileURLToPath } from 'url';
import cors from 'cors';

const app = express();
const port = 3500;


app.use(express.static('public'));
app.use(express.json());
app.use(express.static('public'))

app.use(cors());

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});

app.post('/writeJson', (req, res) => {
    const jsonData = req.body;
    fs.writeFileSync('data.json', JSON.stringify(jsonData));

    res.json({ success: true });
});

app.get('/readJson', (req, res) => {
    try {
        const rawData = fs.readFileSync('data.json');
        const jsonData = JSON.parse(rawData);
        res.json(jsonData);
    } catch (error) {
        console.error('Error reading data.json:', error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});

app.listen(port, () => {
    console.log(`Server is running at http://localhost:${port}`);
});


//node --experimental-modules server.js