//
//  LoginContainerClass.swift
//  iOS_BW-Ventures
//
//  Created by Fhict on 29/09/2017.
//  Copyright © 2017 Lycyic. All rights reserved.
//

import UIKit

class LoginContainerClass: UIViewController {

    @IBOutlet weak var btnLogin: UIButton!
    @IBOutlet var LoginView: UIView!
    @IBOutlet weak var inputUsername: UITextField!
    @IBOutlet weak var inputPassword: UITextField!
    @IBOutlet weak var InnerLoginView: UIView!
    
    let blueWhaleBlue = UIColor(red:0, green: 153/255, blue: 1, alpha: 1)
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        
        inputUsername.center.x -= InnerLoginView.bounds.width
        inputPassword.center.x -= InnerLoginView.bounds.width
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func actionLogin(_ sender: UIButton) {
        //LoginViewHeight.constant = LoginView.bounds.height
        LoginView.backgroundColor = blueWhaleBlue
//        btnLogin.isHidden = true
    }
    

    
    
    
    
    
    
    
    
    
    
    
    
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
