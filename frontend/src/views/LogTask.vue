<template>
    <v-container>
        <v-stepper v-model="e1" vertical>
            <!-- <v-stepper-header> -->
            <v-stepper-step
                :complete="e1 > 1"
                step="1"
            >
                Select project
            </v-stepper-step>

            <v-stepper-content step="1">
                <v-card
                class="mb-12"
                >
                    <v-list dense>
                        <v-subheader>Projects:</v-subheader>
                        <v-list-item-group
                            color="primary"
                        >
                            <v-list-item
                            v-for="(item, i) in projects"
                            :key="i"
                            @click="project = item"
                            >
                            <v-list-item-icon>
                                <v-icon v-text="item.id"></v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.name"></v-list-item-title>
                            </v-list-item-content>
                            </v-list-item>
                        </v-list-item-group>
                    </v-list>
                </v-card>

                <v-btn
                color="primary"
                :disabled="project == null"
                @click="getTasks"
                >
                Next
                </v-btn>

                <v-btn text @click="e1 = 1">
                Back
                </v-btn>
            </v-stepper-content>

            <v-stepper-step
                :complete="e1 > 2"
                step="2"
            >
                Select task
            </v-stepper-step>

            <v-stepper-content step="2">
                <v-card
                class="mb-12"
                >
                    <v-list dense>
                        <v-subheader>Tasks:</v-subheader>
                        <v-list-item-group
                            color="primary"
                        >
                            <v-list-item
                            v-for="(item, i) in tasks"
                            :key="i"
                            @click="task = item"
                            >
                            <v-list-item-icon>
                                <v-icon v-text="item.id"></v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.name"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.project.admin.username"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.project.name"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.project.projectType"></v-list-item-title>
                            </v-list-item-content>
                            </v-list-item>
                        </v-list-item-group>
                    </v-list>
                </v-card>
                <v-btn
                color="primary"
                :disabled="task == null"
                @click="e1 = 3;"
                >
                Next
                </v-btn>
                    
                <v-btn text @click="e1 = 1">
                Back
                </v-btn>
            </v-stepper-content>

            <v-stepper-step
                :complete="e1 > 3"
                step="3"
            >
                Log hours and select day
            </v-stepper-step>

            <v-stepper-content step="3">
                <v-slider
                    v-model="hours"
                    thumb-label="always"
                    ticks
                    max="16"
                    min="0"
                    style="margin:40px;"
                ></v-slider>
                <v-date-picker v-model="dates" multiple :max="today"></v-date-picker>
                <v-text-field
                    v-model="dates"
                    label="Date range"
                    prepend-icon="mdi-calendar"
                    readonly
                ></v-text-field>
                <v-btn
                color="success"
                :disabled="hours === 0 || dates.length === 0"
                @click="log"
                >
                Log task
                </v-btn>
                    
                <v-btn text @click="e1 = 2">
                Back
                </v-btn>
            </v-stepper-content>

            <v-stepper-step
                :complete="e1 > 4"
                step="4"
            >
                Succesfully logged task
            </v-stepper-step>

            <v-stepper-items>

            <v-stepper-content step="4">
                <v-btn
                color="success"
                @click="e1 = 1"
                >
                Log new task
                </v-btn>
            </v-stepper-content>
            </v-stepper-items>
        </v-stepper>
    </v-container>
</template>

<script>

export default {
    name: 'LogTask',
    computed: {

    },
    data() {
        return {
            e1: 1,
            projects: [],
            tasks: [],
            project: null,
            task: null,
            hours: 0,
            dates: [],
            today: null
        }
    },
    methods: {
        getTasks() {
            this.e1 = 2;
            this.axios.get("/api/task/find-all-by-project-employee-id/" + this.project.id, {headers: {'Authorization': `Bearer ` + localStorage.jws}})
                .then(r => {
                    this.tasks = r.data;
                })

        },
        log() {
            this.dates.forEach(d => {
                var time = this.getDateTimeFromString(d, "00:00").getTime();
                this.axios.post("/api/timesheetday/add-by-employee", {date: time, workTime: this.hours, taskId:this.task.id}, {headers: {'Authorization': `Bearer ` + localStorage.jws}})
                .then(() => {
                    this.e1 = 4;
                })
                .catch(() => {
                    this.e1 = 3
                })
            })
        }, 
        // Expected yy-mm-dd and HH:mm format
        getDateTimeFromString: function(dstr, tstr) {
            let dparts = dstr.split('-');
            let tparts = tstr.split(':');
            // -1 because js counts months from 0
            return new Date(dparts[0], dparts[1] - 1, dparts[2], tparts[0], tparts[1]);
        },
        getCurrentDateTime() {
            var today = new Date();
            var dd = String(today.getDate()).padStart(2, '0');
            var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
            var yyyy = today.getFullYear();
            return yyyy + '-' + mm + '-' + dd;
        }
    },
    mounted() {
        this.axios.get("/api/project/find-all-by-user-id/" + this.$store.getters.getUserId, {headers: {'Authorization': `Bearer ` + localStorage.jws}})
            .then(r => {
                this.projects = r.data;
            })
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