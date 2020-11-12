import { CoreModule } from '@alfresco/adf-core';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import {
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatProgressBarModule,
    MatSelectModule,
    MatTableModule
} from '@angular/material';
import { RouterModule } from '@angular/router';
import { CovalentDialogsModule, CovalentMessageModule } from '@covalent/core';
import { LaboratoryResolve, ROUTES } from './services/laboratory.route';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { MatDateFormatModule } from '@lamis/web-core';
import { CustomFormsModule } from 'ng2-validation';
import { LaboratoryDetailsComponent } from './components/laboratory-details.component';
import { LaboratoryEditComponent } from './components/laboratory-edit.component';

@NgModule({
    declarations: [
        LaboratoryDetailsComponent,
        LaboratoryEditComponent
    ],
    imports: [
        CommonModule,
        MatInputModule,
        MatIconModule,
        MatCardModule,
        MatSelectModule,
        MatButtonModule,
        RouterModule.forChild(ROUTES),
        MatProgressBarModule,
        FormsModule,
        CovalentMessageModule,
        CovalentDialogsModule,
        MatTableModule,
        MatListModule,
        CoreModule,
        NgxDatatableModule,
        ReactiveFormsModule,
        MatDateFormatModule,
        CustomFormsModule
    ],
    exports: [
        LaboratoryDetailsComponent,
        LaboratoryEditComponent
    ],
    entryComponents: [],
    providers: [
        LaboratoryResolve
    ]
})
export class LaboratoryModule {
}
