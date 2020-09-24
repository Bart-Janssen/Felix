import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-workshop',
  templateUrl: './workshop.component.html',
  styleUrls: ['./workshop.component.css']
})
export class WorkshopComponent implements OnInit
{
  constructor(private router : Router) { }

  public ngOnInit() : void {}

  protected navigate(page : string) : void
  {
    this.router.navigate([page]);
  }
}