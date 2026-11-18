//! Inventario de la biblioteca del museo.

/// Un ejemplar del catálogo: varios ejemplares pueden tener el mismo ISBN.
pub struct Book {
    pub isbn: String,
    pub copies: u32,
}

/// Suma los ejemplares de cada ISBN, en el orden en que aparece cada uno
/// por primera vez.
pub fn merge_copies(books: &[Book]) -> Vec<(String, u32)> {
    let mut merged: Vec<(String, u32)> = Vec::new();
    for book in books {
        match merged.iter().position(|(isbn, _)| *isbn == book.isbn) {
            Some(i) => merged[i].1 += book.copies,
            None => merged.push((book.isbn.clone(), book.copies)),
        }
    }
    merged
}

/// `n` libros con `n / 2` ISBN distintos: cada ISBN aparece dos veces.
pub fn catalog(n: usize) -> Vec<Book> {
    (0..n)
        .map(|i| Book {
            isbn: format!("978{:010}", i % (n / 2).max(1)),
            copies: 1,
        })
        .collect()
}

#[cfg(test)]
mod tests {
    use super::*;

    fn book(isbn: &str, copies: u32) -> Book {
        Book {
            isbn: isbn.to_string(),
            copies,
        }
    }

    #[test]
    fn adds_copies_of_the_same_isbn() {
        let books = [book("A", 1), book("B", 2), book("A", 3)];
        let want = vec![("A".to_string(), 4), ("B".to_string(), 2)];
        assert_eq!(merge_copies(&books), want);
    }

    #[test]
    fn catalog_repeats_each_isbn_twice() {
        let merged = merge_copies(&catalog(10));
        assert_eq!(merged.len(), 5);
        assert!(merged.iter().all(|(_, copies)| *copies == 2));
    }
}
