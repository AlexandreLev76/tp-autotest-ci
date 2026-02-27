import { Routes } from '@angular/router';
import { BookListComponent } from './components/book-list/book-list.component';
import { BookFormComponent } from './components/book-form/book-form.component';
import { LoginComponent } from './components/login/login.component';
import {AuthGuard} from './helpers/authguard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },

  { path: '', component: BookListComponent, canActivate: [AuthGuard] },
  { path: 'create', component: BookFormComponent, canActivate: [AuthGuard] },
  { path: 'edit/:id', component: BookFormComponent, canActivate: [AuthGuard] },

  // optionnel : redirection si route inconnue
  { path: '**', redirectTo: '' }
];
