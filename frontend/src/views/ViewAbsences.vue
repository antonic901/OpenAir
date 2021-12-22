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
                <v-data-table :headers="headers" :items="filterAbsences"></v-data-table>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
export default {
    name: 'ViewAbsences',
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
                { text: 'Status', sortable: false,  value: 'status' }
            ],
            absences: [],
            status: "INPROCESS"
        }
    },
    mounted() {
        this.axios.get("/users/" + this.$store.getters.getUserId + '/absences', {headers: {'Authorization': `Bearer ` + localStorage.jws}})
            .then(r => {
                this.absences = r.data
            })
    }
}
</script>