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
                <v-data-table :headers="headers" :items="filterAbsences">
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
    </v-container>
</template>

<script>

export default {
    name:'ApproveAbsence',
    computed: {
        filterAbsences() {
            return this.absences.filter(a => {
                if(a.status == this.status) return a;
            })
        }
    },
    data() {
        return {
            headers: [
                { text: 'Name', value: 'employee.name' },
                { text: 'Surname',  value: 'employee.surname' },
                { text: 'Start', value: 'period.startTime' },
                { text: 'End', value: 'period.endTime' },
                { text: 'Status', sortable: false,  value: 'status' },
                { text: 'Actions', align:'center', filterable: false, sortable:false, value: 'additional'}
            ],
            absences: [],
            selectedAbsence: null,
            status: "INPROCESS"
        }
    },
    methods: {
        review(item, status) {
            this.axios.put("/absences/" + item.id, status,{headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ` + localStorage.jws}})
                .then(() => {
                    item.status = status;
                })
        }
    },
    mounted() {
        this.axios.get("/absences/" + this.$store.getters.getUserId, {headers: {'Authorization': `Bearer ` + localStorage.jws}})
            .then(r => {
                this.absences = r.data
            })
    }
}
</script>

<style scoped>

</style>