<template>
    <v-container>
        <v-stepper v-model="e1">
            <v-stepper-header>
            <v-stepper-step
                :complete="e1 > 1"
                step="1"
            >
                Select project
            </v-stepper-step>

            <v-divider></v-divider>

            <v-stepper-step
                :complete="e1 > 2"
                step="2"
            >
                Select employee
            </v-stepper-step>

            <v-divider></v-divider>

            <v-stepper-step
                :complete="e1 > 3"
                step="3"
            >
                Succesfully assigned
            </v-stepper-step>

            </v-stepper-header>

            <v-stepper-items>
            <v-stepper-content step="1">
                <v-card
                class="mb-12"
                >
                    <v-list dense>
                        <v-subheader>PROJECTS:</v-subheader>
                        <v-list-item-group
                            color="primary"
                        >
                            <v-list-item
                            v-for="(item, i) in projects"
                            :key="i"
                            v-on:click="selectProject(item)"
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
                :disabled="selectedProject == null"
                @click="getEmployee"
                >
                Next
                </v-btn>

                <v-btn text @click="e1 = 1">
                Back
                </v-btn>
            </v-stepper-content>

            <v-stepper-content step="2">
                <v-card
                class="mb-12"
                >
                    <v-list dense>
                        <v-subheader>Employees:</v-subheader>
                        <v-list-item-group
                            color="primary"
                        >
                            <v-list-item
                            v-for="(item, i) in emploies"
                            :key="i"
                            v-on:click="selectEmployee(item)"
                            >
                            <v-list-item-icon>
                                <v-icon v-text="item.id"></v-icon>
                            </v-list-item-icon>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.name"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.surname"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.email"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.phone"></v-list-item-title>
                            </v-list-item-content>
                            <v-list-item-content>
                                <v-list-item-title v-text="item.department"></v-list-item-title>
                            </v-list-item-content>
                            </v-list-item>
                        </v-list-item-group>
                    </v-list>
                </v-card>

                <v-btn
                color="primary"
                :disabled="selectedEmployee == null"
                @click="e1 = 3"
                >
                Next
                </v-btn>
                    
                <v-btn text @click="e1 = 1">
                Back
                </v-btn>
            </v-stepper-content>
            <v-stepper-content step="3">
                <v-btn
                color="success"
                @click="assign"
                >
                Assign
                </v-btn>
            </v-stepper-content>
            </v-stepper-items>
        </v-stepper>
        <v-btn v-if="e1 == 4" style="margin-top:20px;" color="success" @click="e1 = 1">Assign new employee</v-btn>
    </v-container>
</template>

<script>

export default {
    name: 'AssignEmloyeeToProject',
    data() {
        return {
            selectedProject: null,
            projects: [],
            selectedEmployee: null,
            emploies: [],
            e1: 1
        }
    },
    methods: {
        getEmployee() {
            this.axios.get("/admin/employees",{headers: {'Authorization': `Bearer ` + localStorage.jws}})
                .then(r => {
                    this.emploies = r.data;
                })
            this.e1 = 2;
        },
        selectProject(item) {
            if(this.selectedProject != null) this.selectedProject = null;
            else this.selectedProject = item;
        },
        selectEmployee(item) {
            if(this.selectedEmployee != null) this.selectedEmployee = null;
            else this.selectedEmployee = item;
        },
        assign() {
            this.e1 = 4;
            var add = {
                employeeId: this.selectedEmployee.id,
                projectId: this.selectedProject.id
            }
            this.axios.put("/projects/" + this.selectedProject.id + "/employees", this.selectedEmployee.id,{headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ` + localStorage.jws}})
        }
    },
    mounted() {
        this.axios.get("/users/" + this.$store.getters.getUserId + "/projects",{headers: {'Authorization': `Bearer ` + localStorage.jws}})
            .then(r => {
                this.projects = r.data;
            })
    }
} 
</script>

<style scoped>

</style>