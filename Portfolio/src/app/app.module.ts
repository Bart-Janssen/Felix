import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing-module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule, HTTP_INTERCEPTORS } from "@angular/common/http";
import { MainComponent } from './components/main/main.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { LearningplanComponent } from './components/learningplan/learningplan.component';
import { RdprojectComponent } from './components/rdproject/rdproject.component';
import { SpecialisationComponent } from './components/specialisation/specialisation.component';
import { ReflectionComponent } from './components/reflection/reflection.component';
import { SampleComponent } from './components/projects/sample/sample.component';
import { EthicsComponent } from './components/projects/ethics/ethics.component';
import { NotimplementedyetComponent } from './components/notimplementedyet/notimplementedyet.component';
import { WorkshopComponent } from './components/projects/workshop/workshop.component';
import { ResearchapproachComponent } from './components/projects/researchapproach/researchapproach.component';
import { ConceptComponent } from './components/projects/concept/concept.component';
import { OsintComponent } from './components/projects/workshop/osint/osint.component';
import { ChatappprojectComponent } from './components/projects/chatappproject/chatappproject.component';

export function jwtOptionsFactory() 
{
  return { tokenGetter: () => 
    {
      return localStorage.getItem('token');
    }
  }
}

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    ProjectsComponent,
    LearningplanComponent,
    RdprojectComponent,
    SpecialisationComponent,
    ReflectionComponent,
    SampleComponent,
    EthicsComponent,
    NotimplementedyetComponent,
    WorkshopComponent,
    ResearchapproachComponent,
    ConceptComponent,
    OsintComponent,
    ChatappprojectComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }