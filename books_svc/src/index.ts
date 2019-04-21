import express from 'express';

const app = express();
const port = 8080;

app.get('/api/v1/book/:id', (req, res) => {
	res.json({
		"book_id": req.params.id,
		"title": "An Absolutely Remarkable Thing",
		"auther": "Hank Green",
		"published_date": "September 25th, 2018"
	})
});

app.listen(port, () => console.log(`Listening on port ${port}!`));
