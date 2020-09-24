import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { isDevMode} from '@angular/core';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit
{
  constructor(private router : Router) { }

  public ngOnInit() : void
  {
    if (isDevMode())
    {
      console.log("devie!");
    }
  }

  protected navigate(page : string) : void
  {
    this.router.navigate([page]);
  }
}