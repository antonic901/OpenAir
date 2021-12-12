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
                Enter basic informations
            </v-stepper-step>

            <v-divider></v-divider>

            <v-stepper-step
                :complete="e1 > 3"
                step="3"
            >
                Succesfully created report
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
                            <v-list-item-content>
                                <v-list-item-title v-text="item.admin.username"></v-list-item-title>
                            </v-list-item-content>
                            </v-list-item>
                        </v-list-item-group>
                    </v-list>
                </v-card>

                <v-btn
                color="primary"
                :disabled="project == null"
                @click="e1 = 2"
                >
                Next
                </v-btn>

                <v-btn text @click="e1 = 1">
                Back
                </v-btn>
            </v-stepper-content>

            <v-stepper-content step="2">
                <v-row>
                    <v-col>
                        <v-form
                            ref="form"
                            v-model="valid"
                            lazy-validation
                            class="text-center"
                        >   
                            <v-text-field
                                v-model="name"
                                :rules="nameRules"
                                label="enter name"
                                placeholder="enter name"
                                required
                                style="margin-left:10px;margin-right:10px;"
                            ></v-text-field>

                            <v-row>
                                <v-col cols="5">
                                    <v-text-field
                                        :prefix="currencySymbol"
                                        type="number"
                                        v-model="price"
                                        :rules="priceRules"
                                        label="enter price"
                                        placeholder="enter price"
                                        required
                                        style="margin-left:10px;margin-right:10px;"
                                    ></v-text-field>
                                </v-col>
                                <v-col>
                                    <v-select
                                    v-model="currency"
                                    :items="currencies"
                                    label="Change currency"
                                    ></v-select>
                                </v-col>
                            </v-row>

                            <v-text-field
                                type="description"
                                v-model="description"
                                :rules="descriptionRules"
                                label="enter description"
                                placeholder="enter description"
                                style="margin-left:10px;margin-right:10px;"
                            ></v-text-field>

                            <v-file-input
                                v-model="image"
                                :rules="imageRules"
                                accept="image/*"
                                label="Select image"
                                chips
                                prepend-icon="mdi-camera"
                                style="margin-left:10px;margin-right:10px;"
                            ></v-file-input>
                            <label v-if="showMessage" style="color:red"><b>{{message}}</b></label>
                        </v-form>
                    </v-col>
                </v-row>
                <v-btn
                color="primary"
                :disabled="!valid"
                @click="createReport"
                >
                Create report
                </v-btn>

                <v-btn text @click="e1 = 1">
                Back
                </v-btn>
            </v-stepper-content>

            <v-stepper-content step="3">
                <v-btn
                color="success"
                @click="remove"
                >
                Create new report
                </v-btn>
            </v-stepper-content>
            </v-stepper-items>
        </v-stepper>
    </v-container>
</template>

<script>
export default {
    name: 'AddExpenseReport',
    computed: {
        currencySymbol() {
            if(this.currency == 'EUR') return '€';
            else return '$'
        }
    },
    data() {
        return {
            e1: 1,
            projects: [],
            project: null,
            currency: 'EUR',
            currencies: ['EUR', 'USD'],
            valid:true,
            message: '',
            showMessage: false,
            name: '',
            nameRules: [
              v => !!v || 'Name is required',
              v => (v && v.length <= 100) || 'Name must be less than 100 characters',
            ],
            price: null,
            priceRules: [
              v => !!v || 'Price is required and must be a number',
            ],
            description: '',
            descriptionRules: [
                v => !!v || 'Description is required',
              v => (v.length == 0 || v.length <= 100) || 'Description must be less than 100 characters'
            ],
            image: null,
            imageRules: [
                v => (this.image != null) || 'Image is required'
            ]
        }
    },
    methods: {
        validate() {
            if(!this.$refs.form.validate()) {
                return;
            }
        },
        selectProject(item) {
            if(this.project == null) this.project = item;
            else this.project = null;
        },
        createReport() {
            var imageName = '';
            var date = (new Date()).getTime();
            const fileToUpload = new FormData();
            imageName = date + '-image-' + this.$store.getters.getUserId + ".jpg";
            fileToUpload.append('file', this.image, imageName);
            var imageUrl = "https://nistagramstorage.s3.eu-central-1.amazonaws.com/" + imageName;
            this.axios.post("/api/upload/upload-file", fileToUpload, {headers: {'Authorization': `Bearer ` + this.$store.getters.getJwt}})
                .catch(e => {
                    alert("Failed to upload to Amazon S3 storage.")
                })
            var create = {
                trackingNumber: 'PX' + Date.now().toString() + '-' + this.$store.getters.getUserId + 'RS',
                name: this.name,
                refund: this.price,
                description: this.description,
                document: imageUrl,
                projectId: this.project.id
            }
            this.axios.post("/api/expensereport/add", create, {headers: {'Authorization': `Bearer ` + this.$store.getters.getJwt}})
                .then(() => {
                    this.e1 = 3
                })
        },
        remove() {
            this.e1 = 1;
            this.projects = this.projects.filter(p => {
                if(p.id != this.project.id) return p;
            })
        }
    },
    mounted() {
        this.axios.get("/api/project/find-all-not-refunded/" + this.$store.getters.getUserId, {headers: {'Authorization': `Bearer ` + this.$store.getters.getJwt}})
            .then(r => {
                this.projects = r.data;
            })

    }
}
</script>

<style scoped>
  .container-column {
    display: flex;
    flex-direction: column;
  }

  .container-column-2 {
    display: flex;
    flex-direction: column;
    align-items: stretch;
  }

  .container-1 {
    display: flex;
    flex-direction: row;
    justify-content: center;
  }

  .container-row {
    display: flex;
    flex-direction: row;
    align-items: stretch;
  }

  .item-1 {
    flex-grow: 1;
  }
</style>