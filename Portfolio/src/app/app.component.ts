import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component(
{
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit
{  
  constructor(private router : Router) {}

  protected showWideNavBar = true;
  protected expandDropdown = false;

  public ngOnInit() : void
  {
    this.resize(window.innerWidth);
    // this.router.navigate(["/osint_workshop"]);
    // this.router.navigate(["/ethics_project"]);
    // this.router.navigate(["/concept_project"]);
    // this.router.navigate(["/research_approach_project"]);
    this.router.navigate(["/main"]);
    // this.router.navigate(["/projects"]);
    // this.router.navigate(["/learningplan"]);
    // this.router.navigate(["/specialisation"]);
  }

  protected onActivate() : void
  {
    window.scroll(0, 0);
  }

  protected onResize(event : any) : void
  {
    this.resize(event.target.innerWidth);
  }

  private resize(currentWindowSize : Number) : void
  {
    this.showWideNavBar = !(currentWindowSize < 752);
  }
  
  protected toggleDropdown() : void
  {
    this.expandDropdown = !this.expandDropdown;
  }

  protected toggleDropdownFalse() : void
  {
    this.expandDropdown = false;
  }
}