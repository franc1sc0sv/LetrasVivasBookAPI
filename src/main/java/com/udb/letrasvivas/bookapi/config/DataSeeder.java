package com.udb.letrasvivas.bookapi.config;

import com.udb.letrasvivas.bookapi.book.model.Book;
import com.udb.letrasvivas.bookapi.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final Random random = new Random();

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() == 0) {
            log.info("🌱 Starting data seeding...");
            seedBooks();
            log.info("✅ Data seeding completed successfully!");
        } else {
            log.info("📚 Database already contains {} books. Skipping seeding.", bookRepository.count());
        }
    }

    private void seedBooks() {
        List<String> titles = Arrays.asList(
                "The Great Gatsby", "To Kill a Mockingbird", "1984", "Pride and Prejudice",
                "The Catcher in the Rye", "The Hobbit", "Moby Dick", "War and Peace",
                "The Odyssey", "Don Quixote", "The Divine Comedy", "The Canterbury Tales",
                "Hamlet", "Macbeth", "Romeo and Juliet", "Othello", "King Lear",
                "The Tempest", "A Midsummer Night's Dream", "The Merchant of Venice",
                "The Adventures of Huckleberry Finn", "The Adventures of Tom Sawyer",
                "The Scarlet Letter", "The House of the Seven Gables", "The Blithedale Romance",
                "The Marble Faun", "The Minister's Black Veil", "Young Goodman Brown",
                "Rappaccini's Daughter", "The Birthmark", "The Artist of the Beautiful",
                "The Celestial Railroad", "The Great Stone Face", "The Snow-Image",
                "The House of Usher", "The Fall of the House of Usher", "The Tell-Tale Heart",
                "The Black Cat", "The Cask of Amontillado", "The Masque of the Red Death",
                "The Pit and the Pendulum", "The Gold-Bug", "The Murders in the Rue Morgue",
                "The Mystery of Marie Rogêt", "The Purloined Letter", "The Balloon-Hoax",
                "The Thousand-and-Second Tale of Scheherazade", "The System of Doctor Tarr",
                "The Facts in the Case of M. Valdemar", "The Sphinx", "The Unparalleled Adventure",
                "The Literary Life of Thingum Bob", "How to Write a Blackwood Article",
                "A Predicament", "The Devil in the Belfry", "Lionizing", "X-ing a Paragrab",
                "Metzengerstein", "The Duc de L'Omelette", "A Tale of Jerusalem",
                "Bon-Bon", "The Assignation", "Berenice", "Morella", "Ligeia",
                "The Man of the Crowd", "The Man That Was Used Up", "The Business Man",
                "The Landscape Garden", "Maelzel's Chess Player", "The Power of Words",
                "The Colloquy of Monos and Una", "Shadow—A Parable", "Silence—A Fable",
                "The Conversation of Eiros and Charmion", "The Island of the Fay",
                "The Oval Portrait", "The Masque of the Red Death", "The Pit and the Pendulum",
                "The Tell-Tale Heart", "The Black Cat", "The Cask of Amontillade",
                "The Gold-Bug", "The Murders in the Rue Morgue", "The Mystery of Marie Rogêt",
                "The Purloined Letter", "The Balloon-Hoax", "The Thousand-and-Second Tale",
                "The System of Doctor Tarr", "The Facts in the Case of M. Valdemar",
                "The Sphinx", "The Unparalleled Adventure", "The Literary Life of Thingum Bob",
                "How to Write a Blackwood Article", "A Predicament", "The Devil in the Belfry",
                "Lionizing", "X-ing a Paragrab", "Metzengerstein", "The Duc de L'Omelette",
                "A Tale of Jerusalem", "Bon-Bon", "The Assignation", "Berenice", "Morella",
                "Ligeia", "The Man of the Crowd", "The Man That Was Used Up", "The Business Man"
        );

        List<String> authors = Arrays.asList(
                "F. Scott Fitzgerald", "Harper Lee", "George Orwell", "Jane Austen",
                "J.D. Salinger", "J.R.R. Tolkien", "Herman Melville", "Leo Tolstoy",
                "Homer", "Miguel de Cervantes", "Dante Alighieri", "Geoffrey Chaucer",
                "William Shakespeare", "Mark Twain", "Nathaniel Hawthorne", "Edgar Allan Poe",
                "Charles Dickens", "Victor Hugo", "Fyodor Dostoevsky", "Anton Chekhov",
                "Oscar Wilde", "Virginia Woolf", "James Joyce", "Ernest Hemingway",
                "F. Scott Fitzgerald", "Harper Lee", "George Orwell", "Jane Austen",
                "J.D. Salinger", "J.R.R. Tolkien", "Herman Melville", "Leo Tolstoy",
                "Homer", "Miguel de Cervantes", "Dante Alighieri", "Geoffrey Chaucer",
                "William Shakespeare", "Mark Twain", "Nathaniel Hawthorne", "Edgar Allan Poe",
                "Charles Dickens", "Victor Hugo", "Fyodor Dostoevsky", "Anton Chekhov",
                "Oscar Wilde", "Virginia Woolf", "James Joyce", "Ernest Hemingway",
                "F. Scott Fitzgerald", "Harper Lee", "George Orwell", "Jane Austen",
                "J.D. Salinger", "J.R.R. Tolkien", "Herman Melville", "Leo Tolstoy",
                "Homer", "Miguel de Cervantes", "Dante Alighieri", "Geoffrey Chaucer",
                "William Shakespeare", "Mark Twain", "Nathaniel Hawthorne", "Edgar Allan Poe",
                "Charles Dickens", "Victor Hugo", "Fyodor Dostoevsky", "Anton Chekhov",
                "Oscar Wilde", "Virginia Woolf", "James Joyce", "Ernest Hemingway",
                "F. Scott Fitzgerald", "Harper Lee", "George Orwell", "Jane Austen",
                "J.D. Salinger", "J.R.R. Tolkien", "Herman Melville", "Leo Tolstoy",
                "Homer", "Miguel de Cervantes", "Dante Alighieri", "Geoffrey Chaucer",
                "William Shakespeare", "Mark Twain", "Nathaniel Hawthorne", "Edgar Allan Poe",
                "Charles Dickens", "Victor Hugo", "Fyodor Dostoevsky", "Anton Chekhov",
                "Oscar Wilde", "Virginia Woolf", "James Joyce", "Ernest Hemingway"
        );

        List<String> genres = Arrays.asList(
                "Fiction", "Classic", "Dystopian", "Romance", "Coming-of-age", "Fantasy",
                "Adventure", "Historical Fiction", "Epic", "Drama", "Mystery", "Horror",
                "Science Fiction", "Thriller", "Literary Fiction", "Poetry", "Biography",
                "Autobiography", "Memoir", "History", "Philosophy", "Religion", "Self-help",
                "Business", "Technology", "Science", "Art", "Music", "Travel", "Cooking",
                "Health", "Fitness", "Psychology", "Education", "Politics", "Economics",
                "Law", "Medicine", "Engineering", "Mathematics", "Physics", "Chemistry",
                "Biology", "Astronomy", "Geology", "Anthropology", "Sociology", "Psychology",
                "Political Science", "International Relations", "Journalism", "Media Studies",
                "Communication", "Marketing", "Finance", "Accounting", "Management",
                "Human Resources", "Operations", "Strategy", "Entrepreneurship", "Innovation"
        );

        List<String> descriptions = Arrays.asList(
                "A timeless classic that explores the human condition.",
                "An epic tale of adventure and discovery.",
                "A profound examination of society and its values.",
                "A beautiful story of love and redemption.",
                "A gripping narrative that keeps you on the edge of your seat.",
                "A masterpiece of literature that has stood the test of time.",
                "An inspiring story of courage and determination.",
                "A thought-provoking exploration of complex themes.",
                "A beautifully written work that touches the heart.",
                "A compelling story that resonates with readers of all ages.",
                "A powerful narrative that challenges conventional thinking.",
                "An engaging tale that combines humor and wisdom.",
                "A moving story that explores the depths of human emotion.",
                "A brilliant work that showcases the author's mastery.",
                "An unforgettable story that leaves a lasting impression.",
                "A captivating tale that draws you in from the first page.",
                "A profound work that offers insights into the human experience.",
                "An entertaining story that also provides food for thought.",
                "A beautifully crafted narrative that showcases literary excellence.",
                "A compelling story that explores important social issues."
        );

        for (int i = 0; i < 100; i++) {
            Book book = new Book();
            book.setTitle(titles.get(random.nextInt(titles.size())));
            book.setAuthor(authors.get(random.nextInt(authors.size())));
            book.setPublicationYear(1800 + random.nextInt(225)); // Years from 1800 to 2024
            book.setDescription(descriptions.get(random.nextInt(descriptions.size())));
            book.setGenre(genres.get(random.nextInt(genres.size())));
            book.setPageCount(100 + random.nextInt(900)); // Pages from 100 to 1000
            book.setPrice(BigDecimal.valueOf(5.99 + random.nextDouble() * 45.01)); // Price from $5.99 to $51.00
            book.setIsAvailable(random.nextBoolean());
            book.setCreatedAt(LocalDateTime.now().minusDays(random.nextInt(365))); // Created within the last year
            book.setUpdatedAt(LocalDateTime.now().minusDays(random.nextInt(30))); // Updated within the last month
            book.setVersion(0L);

            bookRepository.save(book);
        }

        log.info("📚 Successfully seeded 100 books into the database");
    }
}
