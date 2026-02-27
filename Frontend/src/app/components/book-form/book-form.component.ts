import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import {Book} from '../../models/book.model';
import {BookService} from '../../services/book.service';


@Component({
  selector: 'app-book-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './book-form.component.html',
  styleUrls: ['./book-form.component.scss']
})
export class BookFormComponent implements OnInit {

  book: Book = { titre: '', auteur: '', annee: 0 };
  id!: number;
  isEdit = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private bookService: BookService
  ) {}

  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get("id"));
    this.isEdit = !!this.id;

    if (this.isEdit) {
      this.bookService.get(this.id).subscribe((data: any) => this.book = data);
    }
  }

  save() {
    if (this.isEdit) {
      this.bookService.update(this.id, this.book).subscribe(() =>
        this.router.navigate(['/'])
      );
    } else {
      this.bookService.create(this.book).subscribe(() =>
        this.router.navigate(['/'])
      );
    }
  }
}
