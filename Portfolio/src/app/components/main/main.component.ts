import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';

@Component(
{
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})

export class MainComponent implements OnInit
{
  constructor(private http: HttpClient) {}

  public ngOnInit() : void
  {

  }

  protected click() : void
  {
    
  }
}