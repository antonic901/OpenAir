<template>
  <v-container style="max-width:450px;background-color:white;border-radius:20px;margin-top:200px;" fill-height>
    <v-row>
      <v-col>
        <v-card-title primary-title class="justify-center" style="font-size:36px;">
          Welcome
        </v-card-title>
        <v-form
          ref="form"
          v-model="valid"
          lazy-validation
          class="text-center"
        >
          <v-text-field
            v-model="username"
            :rules="usernameRules"
            label="Username"
            required
            style="margin-left:10px;margin-right:10px;"
          ></v-text-field>

          <v-text-field
            type="password"
            v-model="password"
            :rules="passwordRules"
            label="Password"
            required
            style="margin-left:10px;margin-right:10px;"
          ></v-text-field>

          <p v-if="this.messageShow == true" style="color:red;"><b>{{this.message}}</b></p>

          <v-btn
            :disabled="!valid"
            color="success"
            class="mr-4"
            @click="validate"
            style="margin:20px;"
          >
            Sign in
          </v-btn>

        </v-form>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
    name: 'Login',
    data() {
        return {
            valid: true,
            username: '',
            usernameRules: [
              v => !!v || 'Username is required'
            ],
            password: '',
            passwordRules: [
              v => !!v || 'Password is required'
            ],
            message: 'Username or password is not valid!',
            messageShow: false
        }
    },
    methods: {
      validate () {
        if(this.$refs.form.validate()) {
          this.login()
        }
      },
      login() {
        var login = {
          username: this.username,
          password: this.password
        }
        this.axios.post("/auth/login", login)
          .then(r => {
            this.messageShow = false;
            localStorage.jws = r.data.accessToken;
            this.$store.dispatch('updateJwt',r.data.accessToken)
            this.$store.dispatch('updateUserType',r.data.userType)
            this.$store.dispatch('updateUserId',r.data.userId)
            this.$router.push({name: 'Home'});
          })
          .catch(() => {
            this.messageShow = true;
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
