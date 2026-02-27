import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import {BookService} from '../../services/book.service';
import {Book} from '../../models/book.model';


@Component({
  selector: 'app-book-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit {

  books: Book[] = [];

  constructor(private bookService: BookService, private router: Router) {}

  ngOnInit() {
    this.loadBooks();
  }

  loadBooks() {
    this.bookService.getAll().subscribe((data: Book[]) => {
      this.books = data;
    });

  }

  delete(id: number) {
    if (confirm('Supprimer ce livre ?')) {
      this.bookService.delete(id).subscribe(() => this.loadBooks());
    }
  }
}
