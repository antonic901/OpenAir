<template>
    <v-container>
        <v-row>
            <v-col cols="6">
                <v-radio-group
                    v-model="status"
                    row
                    >
                    <v-radio
                        label="Approved"
                        value="APPROVED"
                    ></v-radio>
                    <v-radio
                        label="Rejected"
                        value="REJECTED"
                    ></v-radio>
                    <v-radio
                        label="Awaiting"
                        value="INPROCESS"
                    ></v-radio>
                </v-radio-group>
            </v-col>
            <v-col cols="6">
                <v-radio-group
                    v-model="currency"
                    row
                    v-on:change="convert"
                    >
                    <v-radio
                        label="EURO"
                        value="EUR"
                    ></v-radio>
                    <v-radio
                        label="Dinar"
                        value="RSD"
                    ></v-radio>
                </v-radio-group>
            </v-col>
            <v-col cols="12">
                <v-data-table :headers="headers" :items="filterReports">
                    <template v-slot:item.open_document="{ item }">
                            <v-btn v-on:click="selectedReport = item" @click.stop="showDialog=true" dark color="blue">Open</v-btn>
                    </template>
                    <template v-slot:item.additional="{ item }">
                        <div v-if="item.status == 'INPROCESS'">
                            <v-btn v-on:click="review(item, 'APPROVED')" style="margin:5px;" color="success" >Approve</v-btn>
                            <v-btn v-on:click="review(item, 'REJECTED')" color="error">Reject</v-btn>
                        </div>
                        <v-btn v-else :disabled="true">{{status}}</v-btn>
                    </template>
                </v-data-table>
            </v-col>
        </v-row>
        <DocumentDialog v-model="showDialog" :document=selectedReport ></DocumentDialog>
    </v-container>
</template>

<script>

import DocumentDialog from '../components/DocumentDialog.vue'

export default {
    name: 'ReviewExpenseReport',
    components: {
        DocumentDialog
    },
    computed: {
        filterReports() {
            return this.expenseReports.filter(er => {
                if(er.status == this.status) return er;
            })
            // return reports.filter(r => {
            //     if(r.refund.currency != this.currency) {
            //         r.refund.quantity = this.convert(r.dateOfCreation[0] + "-" + r.dateOfCreation[1] + "-" + r.dateOfCreation[2], r.refund.currency, this.currency, r.refund.quantity);
            //         r.refund.currency = this.currency;
            //         return r;
            //     }
            //     else return r;
            // })
        }
    },
    data() {
        return {
            headers: [
                { text: 'User', value: 'employee.username' },
                { text: 'Tracking number', value: 'trackingNumber' },
                { text: 'Name',  value: 'name' },
                { text: 'Price', value: 'refund.quantity' },
                { text: 'Date of creation', value: 'dateOfCreation' },
                { text: 'Description', value: 'description' },
                { text: 'Document', align:'center', filterable: false, sortable:false, value: 'open_document'},
                { text: 'Status', sortable: false,  value: 'status' },
                { text: 'Actions', align:'center', filterable: false, sortable:false, value: 'additional'}
            ],
            expenseReports: [],
            selectedReport: null,
            status: "INPROCESS",
            showDialog: false,
            currency: 'EUR',
            API_KEY: 'b6020496acea6ff6c2fa3949ae1f7468'
        }
    },
    methods: {
        review(item, status) {
            this.axios.post("/api/expensereport/review/" + item.id + "/" + status, null,{headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ` + this.$store.getters.getJwt}})
                .then(() => {
                    item.status = status;
                })
        },
        open(item) {

        },
        //otkomentarisati 114-130 i zakomentarisati 132-153 ukoliko zelimo da koristimo API (1000 zahteva po mesecu je dozvoljeno)
        convert() {
            // this.expenseReports.filter(er => {
            //     if(er.refund.currency != this.currency) {
            //         this.axios.get("http://api.exchangeratesapi.io/v1/" + er.dateOfCreation[0] + "-" + er.dateOfCreation[1] + "-" + er.dateOfCreation[2] + "?access_key=" + this.API_KEY + "&symbols=RSD")
            //             .then(r => {

            //                 var response = r.data;

            //                 var convertedValue;
                            
            //                 if(er.refund.currency == "EUR") convertedValue = er.refund.quantity * response.rates.RSD;
            //                 else convertedValue = er.refund.quantity / response.rates.RSD;
            //                 er.refund.quantity = convertedValue.toFixed(2);
            //                 er.refund.currency = this.currency;
            //             })
            //     }
            // })

            this.expenseReports.filter(er => {
                if(er.refund.currency != this.currency) {

                    var response = {
                        "success":true,
                        "timestamp":1607558399,
                        "historical":true,
                        "base":"EUR",
                        "date":"2020-12-09",
                        "rates":{
                            "RSD":117.579248
                        }   
                    };

                    var convertedValue;
                    
                    if(er.refund.currency == "EUR") convertedValue = er.refund.quantity * response.rates.RSD;
                    else convertedValue = er.refund.quantity / response.rates.RSD;
                    er.refund.quantity = convertedValue.toFixed(2);
                    er.refund.currency = this.currency;
                }
            })
        }
    },
    mounted() {
        this.axios.get("/api/expensereport/get-all-for-admin", {headers: {'Authorization': `Bearer ` + this.$store.getters.getJwt}})
            .then(r => {
                this.expenseReports = r.data;
            })

    }
}
</script>

<style scoped>

</style>