$(document).ready(function () {
    fetchBooks();

    // Fetch all books
    function fetchBooks() {
        $.ajax({
            url: '/getAllBooks',
            type: 'GET',
            success: function (books) {
                let bookListHtml = '<ul class="list-group">';
                books.forEach(book => {
                    let stars = getStars(book.rating);

                    bookListHtml += `<li class="list-group-item">
                    <img src="${book.coverUrl}" alt="Book Cover" width="50" height="75" class="mr-3" />
                    <strong>${book.title}</strong> by ${book.author}
                    <span class="float">${stars}</span>
                    <button class="btn btn-danger btn-sm float-right ml-3" onclick="deleteBook(${book.id})">Delete</button>
                    <button class="btn btn-primary btn-sm float-right" onclick="openUpdateBookPage(${book.id})">Update</button>
                </li>`;
                });
                bookListHtml += '</ul>';
                $('#bookList').html(bookListHtml);
            },
            error: function () {
                $('#bookList').html('<p>No books added or Error fetching books.</p>');
            }
        });
    }

    // Function to generate star rating
    function getStars(rating) {
        let stars = '';
        for (let i = 1; i <= 5; i++) {
            stars += i <= rating ? '<i class="fas fa-star"></i>' : '<i class="far fa-star"></i>';
        }
        return stars;
    }

    // Add a new book
    $('#addBookForm').submit(function (event) {
        event.preventDefault();
        const bookData = {
            title: $('#title').val(),
            author: $('#author').val(),
            rating: $('#rating').val(),
        };

        $.ajax({
            url: '/addBook',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(bookData),
            success: function (response) {
                alert('Book added successfully!');
                $('#addBookForm')[0].reset();
                fetchBooks(); // Refresh book list
            },
            error: function () {
                alert('Error adding book.');
            }
        });
    });
});

// Redirect to updateBook.html by book id
function openUpdateBookPage(bookId) {
    window.location.href = `updateBook.html?id=${bookId}`;
}

// Delete a book by ID
function deleteBook(id) {
    $.ajax({
        url: `/deleteBookById/${id}`,
        type: 'DELETE',
        success: function () {
            alert('Book deleted successfully!');
            fetchBooks(); // Refresh book list
        },
        error: function () {
            alert('Error deleting book.');
        }
    });
}
