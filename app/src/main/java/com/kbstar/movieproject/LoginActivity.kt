package com.kbstar.movieproject

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kbstar.movieproject.databinding.ActivityLoginBinding
import com.kbstar.movieproject.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity() {

    lateinit var bindingLogin: ActivityLoginBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingLogin = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingLogin.root)

        //ViewModel 은 JetPack 에서 제공하는 기법중 하나로..
        //앱의 코드를 MVVM(Model-View-ViewModel) 패턴을 적용하겠다는 의미이다..
        //viewmodel 은 안드로이드에서 MVVM 을 지원하기 위해서 제공되는 기법이다.
        //핵심은 activity 는 UI(View)만 구성하겠다는 의미..
        //activity 에서 필요한 업무처리(Model)을 ViewModel을 이용해 activity에서 분리해서 개발하겠다는 의도..
        val viewModel: LoginViewModel by viewModels<LoginViewModel>()

        bindingLogin.btnLogin.setOnClickListener{
            viewModel.login(bindingLogin.etId.text.toString(), bindingLogin.etPass.text.toString())
//            login()
        }

        viewModel.loginLiveData.observe(this) {
            if(it == "ok"){
                finish()
            }
        }
    }


}