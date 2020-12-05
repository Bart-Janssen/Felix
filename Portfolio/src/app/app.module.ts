import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing-module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { MainComponent } from './components/main/main.component';
import { ProjectsComponent } from './components/projects/projects.component';
import { LearningplanComponent } from './components/learningplan/learningplan.component';
import { RdprojectComponent } from './components/rdproject/rdproject.component';
import { SpecialisationComponent } from './components/specialisation/specialisation.component';
import { ReflectionComponent } from './components/reflection/reflection.component';
import { EthicsComponent } from './components/projects/ethics/ethics.component';
import { NotimplementedyetComponent } from './components/notimplementedyet/notimplementedyet.component';
import { WorkshopComponent } from './components/projects/workshop/workshop.component';
import { ResearchapproachComponent } from './components/projects/researchapproach/researchapproach.component';
import { ConceptComponent } from './components/projects/felix/concept/concept.component';
import { OsintComponent } from './components/projects/workshop/osint/osint.component';
import { ChatappprojectComponent } from './components/projects/felix/chatappproject/chatappproject.component';
import { ThreatanalysisComponent } from './components/projects/felix/threatanalysis/threatanalysis.component';
import { RiskanalysisComponent } from './components/projects/felix/riskanalysis/riskanalysis.component';
import { FelixComponent } from './components/projects/felix/felix.component';
import { RedblueeventComponent } from './components/projects/felix/redblueevent/redblueevent.component';
import { PreventtheunexpectedComponent } from './components/projects/workshop/preventtheunexpected/preventtheunexpected.component';
import { QuantumNetworkingComponent } from './components/projects/quantum-networking/quantum-networking.component';
import { ReverseengeneeringComponent } from './components/projects/workshop/reverseengeneering/reverseengeneering.component';
import { SecuritytestingComponent } from './components/projects/felix/securitytesting/securitytesting.component';
import { OnderzoeksplanComponent } from './components/projects/onderzoeksplan/onderzoeksplan.component';
import { RsaComponent } from './components/projects/rsa/rsa.component';
import { LicensingComponent } from './components/projects/felix/licensing/licensing.component';
import { CompilingComponent } from './components/projects/compiling/compiling.component';

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
    EthicsComponent,
    NotimplementedyetComponent,
    WorkshopComponent,
    ResearchapproachComponent,
    ConceptComponent,
    OsintComponent,
    ChatappprojectComponent,
    ThreatanalysisComponent,
    RiskanalysisComponent,
    FelixComponent,
    RedblueeventComponent,
    PreventtheunexpectedComponent,
    QuantumNetworkingComponent,
    ReverseengeneeringComponent,
    SecuritytestingComponent,
    OnderzoeksplanComponent,
    RsaComponent,
    LicensingComponent,
    CompilingComponent,
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