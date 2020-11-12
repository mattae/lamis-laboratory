import { Component, ComponentFactoryResolver, OnDestroy, OnInit } from '@angular/core';
import { Laboratory, LaboratoryLine } from '../model/laboratory.model';
import { ActivatedRoute, Router } from '@angular/router';
import { LaboratoryService } from '../services/laboratory.service';
import { TdDialogService } from '@covalent/core';
import {
    CardViewDateItemModel,
    CardViewItem,
    CardViewTextItemModel,
    CardViewUpdateService,
    NotificationService
} from '@alfresco/adf-core';
import { ColumnMode } from '@swimlane/ngx-datatable';

@Component({
    selector: 'lamis-laboratory',
    templateUrl: './laboratory-details.component.html'
})
export class LaboratoryDetailsComponent implements OnInit, OnDestroy {
    properties: CardViewItem[] = [];
    entity: Laboratory;
    ColumnMode = ColumnMode;
    public dataSource: LaboratoryLine[];

    constructor(private router: Router, private route: ActivatedRoute, private laboratoryService: LaboratoryService,
                private cfr: ComponentFactoryResolver, private _dialogService: TdDialogService,
                private notificationService: NotificationService,
                private updateService: CardViewUpdateService) {
    }

    ngOnInit() {
        this.route.data.subscribe(({entity}) => {
            this.entity = !!entity && entity.body ? entity.body : entity;
            const patientId = this.route.snapshot.paramMap.get('patientId');
            this.laboratoryService.getPatient(patientId).subscribe((res) => this.entity.patient = res);
            this.buildProperties();
        });
    }

    edit() {
        this.router.navigate(['/', 'laboratories', this.entity.uuid, 'patient', this.entity.patient.uuid, 'edit']);
    }

    delete() {
        this._dialogService.openConfirm({
            title: 'Confirm',
            message: 'Do you want to delete this laboratory request, action cannot be reversed?',
            cancelButton: 'No',
            acceptButton: 'Yes',
            width: '500px',
        }).afterClosed().subscribe((accept: boolean) => {
            if (accept) {
                this.laboratoryService.delete(this.entity.id).subscribe((res) => {
                    if (res.ok) {
                        this.router.navigate(['patients']);
                    } else {
                        this.notificationService.showError('Error deleting visit, please try again');
                    }
                });
            } else {
                // DO SOMETHING ELSE
            }
        });
    }

    buildProperties() {
        this.properties.push(new CardViewDateItemModel({
            key: 'sc',
            value: this.entity.dateSampleCollected,
            label: 'Date of Sample Collected',
            format: 'dd MMM, yyyy'
        }));

        this.properties.push(new CardViewDateItemModel({
            key: 'ds',
            value: this.entity.dateAssay,
            label: 'Date of Assay',
            format: 'dd MMM, yyyy'
        }));

        this.properties.push(new CardViewDateItemModel({
            key: 'na',
            value: this.entity.dateResultReceived,
            label: 'Date Result Received',
            format: 'dd MMM, yyyy'
        }));
        this.properties.push(new CardViewTextItemModel({
            label: 'Laboratory Number',
            key: 'fs',
            value: this.entity.labNo
        }));

        /*this.laboratoryService.getLinesByLaboratory(this.entity.id)
            .subscribe(res => {
                this.dataSource = res;
            });*/
        this.dataSource = [...this.entity.lines.map(r => {
            this.laboratoryService.getLabTestById(r.lab_test_id).subscribe(res => {
                r.description = res.description;
            });
            return r;
        })];
    }

    previousState() {
        window.history.back();
    }

    public ngOnDestroy() {
    }
}
