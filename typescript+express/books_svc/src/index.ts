import express from 'express';

const app = express();
const port = 8080;

app.get('/api/v1/book/:id', (req, res) => {
	res.json({
		"book_id": req.params.id,
		"title": "An Absolutely Remarkable Thing",
		"author": "Hank Green",
		"published_date": "2018-12-25"
	})
});

app.listen(port, () => console.log(`Listening on port ${port}!`));
