//
//  ViewController.swift
//  ventures
//
//  Created by Fhict on 30/10/2017.
//  Copyright © 2017 Restart. All rights reserved.
//

import UIKit
import Firebase

class LoginScreen: UIViewController {
    
    @IBOutlet weak var tbEmailAddress: UITextField!
    @IBOutlet weak var tbPassword: UITextField!
    
    var email = ""
    var password = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }
    
//    override func viewWillAppear(_ animated: Bool) {
//        Auth.auth().addStateDidChangeListener { (auth, user) in
//            if user != nil {
//                let storyboard : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
//                let nextViewController = storyboard.instantiateViewController(withIdentifier: "tabBarID")
//                self.present(nextViewController, animated: true, completion: nil)
//            }
//        }
//    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func loginAction(sender: UIButton) {
        email = tbEmailAddress.text!
        password = tbPassword.text!
        
        let storyboard : UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let nextViewController = storyboard.instantiateViewController(withIdentifier: "tabBarID")
        
        if self.tbEmailAddress.text == "" || self.tbPassword.text == "" {
            // Alert to tell the user that there was an error because they didn't fill anything in one or both textfields
            let alertController = UIAlertController(title: "Error", message: "Please enter and email address and password", preferredStyle: .alert)
            
            let defaultAction = UIAlertAction(title: "OK", style: .cancel, handler: nil)
            alertController.addAction(defaultAction)
            
            self.present(alertController, animated: true, completion: nil)
        } else {
            Auth.auth().signIn(withEmail: email, password: password) {(user, error) in
                
                if error == nil {
                    // Print to console
                    let db = Firestore.firestore()
                    let docRef = db.collection("users").document((user?.uid)!)
                    docRef.getDocument { (document, error) in
                        if let document = document {
                            // Create instance of UserDefaults
                            let userDef = UserDefaults.standard
                            // Storing values
                            userDef.set(document.data()["age"], forKey: "age")
                            userDef.set(document.data()["email"], forKey: "email")
                            userDef.set(document.data()["gender"], forKey: "gender")
                            userDef.set(document.data()["name"], forKey: "name")
                            userDef.set(document.data()["phoneNumber"], forKey: "phoneNumber")
                            userDef.set(document.data()["uid"], forKey: "uid")
                            userDef.set(document.data()["ventureID"], forKey: "ventureid")
                        } else {
                            print("Document does not exist")
                        }
                    }
                    self.present(nextViewController, animated: true, completion: nil)
                } else {
                    // Tell user that there is an error
                    let alertController = UIAlertController(title: "Error", message: error?.localizedDescription, preferredStyle: .alert)
                    
                    let defaultAction = UIAlertAction(title: "OK", style: .cancel, handler: nil)
                    alertController.addAction(defaultAction)
                    
                    self.present(alertController, animated: true, completion: nil)
                }
            }
        }
    }
}

