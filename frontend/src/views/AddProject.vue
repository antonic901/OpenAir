<template>
    <v-container>
        <v-row>
            <v-col>
                <v-card-title primary-title class="justify-center" style="font-size:36px;">
                Add new project
                </v-card-title>
                <v-form
                ref="form"
                v-model="valid"
                lazy-validation
                class="text-center"
                >
                    <v-text-field
                        v-model="name"
                        :rules="nameRules"
                        label="enter project name"
                        required
                        style="margin-left:10px;margin-right:10px;"
                    ></v-text-field>
                    <v-radio-group
                    v-model="projectType"
                    row
                    :rules="projectTypeRules"
                    >
                        <v-radio
                            label="Intern"
                            value="INTERN"
                        ></v-radio>
                        <v-radio
                            label="Commercial"
                            value="COMMERCIAL"
                        ></v-radio>
                    </v-radio-group>
                    <label v-if="messageShow" style="color:red;margin-left:20px"><b>{{message}}</b></label>
                </v-form>
                <v-btn
                    :disabled="!valid"
                    color="success"
                    class="mr-4"
                    @click="validate"
                    style="margin:20px;"
                >
                    CREATE
                </v-btn>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>

import axios from 'axios'

export default {
    name: 'AddProject',
    data() {
        return {
            valid: true,
            name: '',
            nameRules: [
              v => !!v || 'Name is required'
            ],
            projectType: null,
            projectTypeRules: [
                v => !!v || 'Project type is required'
            ],
            message: '',
            messageShow: false
        }
    },
    methods: {
        validate () {
            if(this.$refs.form.validate()) {
              this.create();
            }
        },
        create() {
          axios.post("http://localhost:8081/api/project/add", {name: this.name, projectType: this.projectType}, {headers: {'Authorization': `Bearer ` + this.$store.getters.getJwt}})
            .then(() => {
                alert("New project is sucessfuly added.");
                this.message = '';
                this.messageShow = false;
                this.name = '';
            })
            .catch(() => {
                this.message = 'Project with same name exist';
                this.messageShow = true;
            })
        }
    }
}
</script>

<style scoped>

</style>>