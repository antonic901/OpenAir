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
                Choose name
            </v-stepper-step>

            <v-divider></v-divider>

            <v-stepper-step
                :complete="e1 > 4"
                step="4"
            >
                Succesfully created task
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
                <v-text-field
                    v-model="name"
                    color="blue"
                    label="Task name"
                    placeholder="enter name of task"
                    required
                    ></v-text-field>
                <v-btn
                color="primary"
                :disabled="name == ''"
                v-on:input="checkIsNameValid"
                @click="e1 = 4"
                >
                Next
                </v-btn>
                    
                <v-btn text @click="e1 = 2">
                Back
                </v-btn>
            </v-stepper-content>
            <v-stepper-content step="4">
                <v-btn
                color="success"
                @click="assign"
                >
                Create task
                </v-btn>
            </v-stepper-content>
            </v-stepper-items>
        </v-stepper>
        <v-btn v-if="e1 == 5" style="margin-top:20px;" color="success" @click="e1 = 1">Create new task</v-btn>
    </v-container>
</template>

<script>

import axios from 'axios'

export default {
    name: 'AddTask',
    data() {
        return {
            name: '',
            selectedProject: null,
            projects: [],
            selectedEmployee: null,
            emploies: [],
            e1: 1
        }
    },
    methods: {
        getEmployee() {
            axios.get("http://localhost:8081/api/admin/getEmployees",{headers: {'Authorization': `Bearer ` + this.$store.getters.getJwt}})
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
            var add = {
                name: this.name,
                employeeID: this.selectedEmployee.id,
                projectID: this.selectedProject.id
            }
            axios.post("http://localhost:8081/api/task/addTask", add ,{headers: {'Authorization': `Bearer ` + this.$store.getters.getJwt}})
                .then(() => {
                    this.e1 = 5;
                    this.name = '';
                })
                .catch(() => {
                    this.e1 = 3;
                    var name = this.name;
                    this.name = 'Name ' + name + ' is already taken.';
                })
        },
        checkIsNameValid() {

        }
    },
    mounted() {
        axios.get("http://localhost:8081/api/project/findAllByUserId/1",{headers: {'Authorization': `Bearer ` + this.$store.getters.getJwt}})
            .then(r => {
                this.projects = r.data;
            })
    }
} 
</script>

<style scoped>

</style>