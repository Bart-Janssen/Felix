import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { MainComponent } from './components/main/main.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { RdprojectComponent } from './components/rdproject/rdproject.component';
import { LearningplanComponent } from './components/learningplan/learningplan.component';
import { SpecialisationComponent } from './components/specialisation/specialisation.component';
import { ReflectionComponent } from './components/reflection/reflection.component';
import { EthicsComponent } from './components/projects/ethics/ethics.component';
import { NotimplementedyetComponent } from './components/notimplementedyet/notimplementedyet.component';
import { WorkshopComponent } from './components/projects/workshop/workshop.component';
import { ResearchapproachComponent } from './components/projects/researchapproach/researchapproach.component';
import { ConceptComponent } from './components/projects/felix/concept/concept.component';
import { OsintComponent } from './components/projects/workshop/osint/osint.component';
import { FelixComponent } from './components/projects/felix/felix.component';
import { ChatappprojectComponent } from './components/projects/felix/chatappproject/chatappproject.component';
import { ThreatanalysisComponent } from './components/projects/felix/threatanalysis/threatanalysis.component';
import { RiskanalysisComponent } from './components/projects/felix/riskanalysis/riskanalysis.component';
import { RedblueeventComponent } from './components/projects/felix/redblueevent/redblueevent.component';
import { PreventtheunexpectedComponent } from './components/projects/workshop/preventtheunexpected/preventtheunexpected.component';
import { QuantumNetworkingComponent } from './components/projects/quantum-networking/quantum-networking.component';
import { ReverseengeneeringComponent } from './components/projects/workshop/reverseengeneering/reverseengeneering.component';
import { SecuritytestingComponent } from './components/projects/felix/securitytesting/securitytesting.component';
import { OnderzoeksplanComponent } from './components/projects/onderzoeksplan/onderzoeksplan.component';
import { RsaComponent } from './components/projects/rsa/rsa.component';
import { LicensingComponent } from './components/projects/felix/licensing/licensing.component';
import { CompilingComponent } from './components/projects/compiling/compiling.component';
import { FeedbackComponent } from './components/feedback/feedback.component';
import { PersoonlijkeOntwikkelingComponent } from './components/persoonlijke-ontwikkeling/persoonlijke-ontwikkeling.component';
import { AsmComponent } from './components/projects/asm/asm.component';

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
    path: "feedback",
    component: FeedbackComponent
  },
  {
    path: "po",
    component: PersoonlijkeOntwikkelingComponent
  },
  {
    path: "ethics_project",
    component: EthicsComponent,
  },
  {
    path: "onderzoeksplan_project",
    component: OnderzoeksplanComponent
  },
  {
    path: "research_approach_project",
    component: ResearchapproachComponent,
  },
  {
    path: "rsa_project",
    component: RsaComponent
  },
  {
    path: "compiling_project",
    component: CompilingComponent
  },
  {
    path: "asm_project",
    component: AsmComponent
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
    path: "prevent_the_unexpected_workshop",
    component: PreventtheunexpectedComponent
  },
  {
    path: "reverse_engineering_workshop",
    component: ReverseengeneeringComponent
  },
  {
    path: "quantum_networking",
    component: QuantumNetworkingComponent
  },
  {
    path: "felix",
    component: FelixComponent,
  },
  {
    path: "concept",
    component: ConceptComponent,
  },
  {
    path: "implementation",
    component: ChatappprojectComponent
  },
  {
    path: "threat_analysis",
    component: ThreatanalysisComponent
  },
  {
    path: "risk_analysis",
    component: RiskanalysisComponent
  },
  {
    path: "red_team_vs_blue_team_event",
    component: RedblueeventComponent
  },
  {
    path: "security_testing",
    component: SecuritytestingComponent
  },
  {
    path: "licensing",
    component: LicensingComponent
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