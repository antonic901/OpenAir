<template>
    <v-container>
        <v-row>
            <v-col cols="12">
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

import axios from 'axios'

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
        }
    },
    data() {
        return {
            headers: [
                { text: 'User', value: 'employee.username' },
                { text: 'Tracking number', value: 'trackingNumber' },
                { text: 'Name',  value: 'name' },
                { text: 'Price', value: 'refund' },
                { text: 'Date of creation', value: 'dateOfCreation' },
                { text: 'Description', value: 'description' },
                { text: 'Document', align:'center', filterable: false, sortable:false, value: 'open_document'},
                { text: 'Status', sortable: false,  value: 'status' },
                { text: 'Actions', align:'center', filterable: false, sortable:false, value: 'additional'}
            ],
            expenseReports: [],
            selectedReport: null,
            status: "INPROCESS",
            showDialog: false
        }
    },
    methods: {
        review(item, status) {
            axios.post("http://localhost:8081/api/expensereport/review/" + item.id + "/" + status, null,{headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ` + this.$store.getters.getJwt}})
                .then(() => {
                    item.status = status;
                })
        },
        open(item) {

        }
    },
    mounted() {
        axios.get("http://localhost:8081/api/expensereport/get-all-for-admin", {headers: {'Authorization': `Bearer ` + this.$store.getters.getJwt}})
            .then(r => {
                this.expenseReports = r.data;
            })

    }
}
</script>

<style scoped>

</style>