import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-felix',
  templateUrl: './felix.component.html',
  styleUrls: ['./felix.component.css']
})
export class FelixComponent implements OnInit
{
  constructor(private router : Router) { }

  public ngOnInit() : void {}

  protected navigate(page : string) : void
  {
    this.router.navigate([page]);
  }
}