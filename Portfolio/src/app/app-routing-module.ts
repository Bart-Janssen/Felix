import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './components/main/main.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { RdprojectComponent } from './components/rdproject/rdproject.component';
import { LearningplanComponent } from './components/learningplan/learningplan.component';
import { SpecialisationComponent } from './components/specialisation/specialisation.component';
import { ReflectionComponent } from './components/reflection/reflection.component';
import { SampleComponent } from './components/projects/sample/sample.component';
import { EthicsComponent } from './components/projects/ethics/ethics.component';
import { NotimplementedyetComponent } from './components/notimplementedyet/notimplementedyet.component';
import { WorkshopComponent } from './components/projects/workshop/workshop.component';
import { ResearchapproachComponent } from './components/projects/researchapproach/researchapproach.component';
import { ConceptComponent } from './components/projects/concept/concept.component';
import { OsintComponent } from './components/projects/workshop/osint/osint.component';

const routes: Routes = [
  {
    path: '',
    component: MainComponent,
    pathMatch: 'full',
  },
  {
    path: "main",
    component: MainComponent,
  },
  {
    path: "projects",
    component: ProjectsComponent,
  },
  {
    path: "rdproject",
    component: RdprojectComponent,
  },
  {
    path: "learningplan",
    component: LearningplanComponent,
  },
  {
    path: "specialisation",
    component: SpecialisationComponent,
  },
  {
    path: "reflection",
    component: ReflectionComponent,
  },
  {
    path: "sample",
    component: SampleComponent,
  },
  {
    path: "ethics_project",
    component: EthicsComponent,
  },
  {
    path: "workshop_project",
    component: WorkshopComponent,
  },
  {
    path: "osint_workshop",
    component: OsintComponent,
  },
  {
    path: "research_approach_project",
    component: ResearchapproachComponent,
  },
  {
    path: "concept_project",
    component: ConceptComponent,
  },
  {
    path: "not-implemented-yet",
    component: NotimplementedyetComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}