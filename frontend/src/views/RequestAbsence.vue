<template>
    <v-container>
        <v-card class="mx-auto my-12" max-width="374">
            <v-card-title class="justify-center">Request absence:</v-card-title>
            <v-card-text>
                <v-row>
                    <v-col>
                        <v-date-picker v-model="dates" range :min="today"></v-date-picker>
                    </v-col>
                    <v-col>
                        <v-text-field
                            v-model="dateRangeText"
                            label="Date range"
                            prepend-icon="mdi-calendar"
                            readonly
                        ></v-text-field>
                    </v-col>
                </v-row>
            </v-card-text>
            <v-card-actions>
                <v-btn
                    color="blue"
                    text
                    @click="request"
                    :disabled="disable"
                >
                    Request
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-container>
</template>

<script>

export default {
    name:'RequestAbsence',
    computed: {
      dateRangeText () {
        return this.dates.join(' ~ ')
      },
      disable() {
          if(this.dates.length != 2) return true;
          else if (this.dates[0] > this.dates[1]) return true;
          else return false;
      }
    },
    data: () => ({
      dates: [],
      today: null
    }),
    methods: {
        request() {
            var period = {
                startTime: this.getDateTimeFromString(this.dates[0], "00:00").getTime(),
                endTime: this.getDateTimeFromString(this.dates[1], "00:00").getTime()
            }
            this.axios.post("/absences", period, {headers: {'Authorization': `Bearer ` + localStorage.jws}})
                .then(() => {
                    alert("Succesfully requested.");
                    this.dates = [];
                })
                .catch(e => {
                    alert(e.response.data)
                    this.dates = [];
                })
        },
        // Expected yy-mm-dd and HH:mm format
        getDateTimeFromString: function(dstr, tstr) {
            let dparts = dstr.split('-');
            let tparts = tstr.split(':');
            // -1 because js counts months from 0
            return new Date(dparts[0], dparts[1] - 1, dparts[2], tparts[0], tparts[1]);
        }
    },
    mounted() {
        var today = new Date();
        var dd = String(today.getDate()).padStart(2, '0');
        var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
        var yyyy = today.getFullYear();
        this.today = yyyy + '-' + mm + '-' + dd;
    }
}
</script>

<style scoped>

</style>