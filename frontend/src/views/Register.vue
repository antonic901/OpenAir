<template>
  <v-container style="max-width:600px;background-color:white;border-radius:20px;margin-top:50px;" fill-height>
    <v-row>
      <v-col>
        <v-card-title primary-title class="justify-center" style="font-size:36px;">
          Register new Employee
        </v-card-title>
        <v-form
          ref="form"
          v-model="valid"
          lazy-validation
          class="text-center"
        >
          <v-text-field
            v-model="user.username"
            v-on:input="checkIsUsernameValid"
            :rules="usernameRules"
            label="enter username"
            required
            style="margin-left:10px;margin-right:10px;"
          ></v-text-field>

          <v-text-field
            type="password"
            v-model="user.password"
            :rules="passwordRules"
            label="enter password"
            required
            style="margin-left:10px;margin-right:10px;"
          ></v-text-field>

          <v-text-field
            type="password"
            v-model="user.confirmPassword"
            :rules="confirmPasswordRules"
            label="confirm password"
            required
            style="margin-left:10px;margin-right:10px;"
          ></v-text-field>

          <v-text-field
            v-model="user.email"
            :rules="emailRules"
            label="enter email"
            required
            style="margin-left:10px;margin-right:10px;"
          ></v-text-field>

          <v-text-field
            v-model="user.name"
            :rules="nameRules"
            label="enter name"
            required
            style="margin-left:10px;margin-right:10px;"
          ></v-text-field>

          <v-text-field
            v-model="user.surname"
            :rules="surnameRules"
            label="enter surname"
            required
            style="margin-left:10px;margin-right:10px;"
          ></v-text-field>

          <v-text-field
            v-model="user.phone"
            :rules="phoneRules"
            label="enter phone"
            required
            style="margin-left:10px;margin-right:10px;"
          ></v-text-field>

            <v-menu offset-y>
                <template v-slot:activator="{ on, attrs }">
                    <v-btn v-if="user.department == null || user.department == ''" color="primary" dark v-bind="attrs" v-on="on">
                        Select type
                        <v-icon>mdi-chevron-down</v-icon>
                    </v-btn>
                    <v-btn v-else color="primary" dark v-bind="attrs" v-on="on">
                        {{user.department}}
                        <v-icon>mdi-chevron-down</v-icon>
                    </v-btn>
                </template>
                <v-list>
                    <v-list-item v-for="(item, index) in departments" :key="index" v-on:click="user.department = item">
                        <v-list-item-title>{{ item }}</v-list-item-title>
                    </v-list-item>
                </v-list>
            </v-menu>
            <label v-if="messageShow" style="color:red;margin-left:20px"><b>{{message}}</b></label>
        </v-form>
        
        <v-btn
            :disabled="!valid"
            color="success"
            class="mr-4"
            @click="validate"
            style="margin:20px;"
          >
            Register
          </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>

export default {
    name: 'Registration',
    data() {
        return {
            message: '',
            messageShow: false,
            menu: false,
            valid: true,
            validUsername: true,
            user: {
                username: '',
                password: '',
                confirmPassword: '',
                email: '',
                name: '',
                surname: '',
                phone: null,
                userType: "ROLE_EMPLOYEE",
                department: null,
                salary: 0
            },
            departments: ['JAVA', 'dotNET', 'JAVASCRIPT']
            ,
            usernameRules: [
              v => !!v || 'Username is required',
              v => (v && v.length <= 15) || 'Username must be less than 10 characters',
              v => (v && this.validUsername) || 'This username is already taken'
            ],
            passwordRules: [
              v => !!v || 'Password is required',
              v => (v && v.length >= 5) || 'Password must have at least 5 characters'
            ],
            confirmPasswordRules: [
              v => !!v || 'Password is required',
              v => (v && this.user.password == this.user.confirmPassword) || 'Passwords do not match'
            ],
            emailRules: [
              v => !!v || 'Email is required',
              v => !v || /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/.test(v) || 'E-mail must be valid'
            ],
            nameRules: [
                v => !!v || 'Name is required'
            ],
            surnameRules: [
                v => !!v || 'Surname is required'
            ],
            phoneRules: [
                v => !!v || 'Phone is required',
                v => !v || /^\d+$/.test(v) || 'Phone number is not valid'
            ],
            departmentRules: [
                v => !!v || 'Department is required'
            ]
        }
    },
    methods: {
        validate () {
            if(this.$refs.form.validate()) {
              if(this.user.department == null) {
                this.message = 'You must select department';
                this.messageShow = true;
              } 
              else this.register();
            }
        },
        checkIsUsernameValid() {
          if(this.user.username == '') return;
          this.axios.get("/auth/check-username/" + this.user.username)
            .then(r => {
              if(!r.data) {
                this.validUsername = false
                this.$refs.form.validate()
              }
              else {
                this.validUsername = true
                this.$refs.form.validate()
              }
            })
        },
        register() {
          this.messageShow = false;
          var user = this.user;
          this.axios.post("/employees", user, {headers: {'Authorization': `Bearer ` + localStorage.jws}})
            .then(() => {
              alert("New employee is sucessfuly added.");
              this.user.username = '';
            })
            .catch(() => {
              alert("Something went wrong.")
            })
        }
    }
}
</script>

<style scoped>

  .container-column {
    display: flex;
    flex-direction: column;
  }

  .container-1 {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
  }

  .container-row {
    display: flex;
    flex-direction: row;
  }

  .item-1 {
    flex-grow: 1;
  }

</style>