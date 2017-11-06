//
//  ExperimentsClass.swift
//  ventures
//
//  Created by Fhict on 31/10/2017.
//  Copyright © 2017 Restart. All rights reserved.
//

import UIKit
import Firebase

class ExperimentsClass: UIViewController {

    @IBOutlet weak var experimentsTableView: UITableView!

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        collectExperiments()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func collectExperiments() {
        let db = Firestore.firestore()
        let userDef = UserDefaults.standard
        let ventureid = userDef.string(forKey: "ventureid")
        let docRef = db.collection("Startups").document(ventureid!).collection("Experiments")
        
        docRef.getDocuments() { (querySnapshot, err) in
            if let err = err {
                print("Error getting documents: \(err)")
            } else {
                for document in querySnapshot!.documents {
                    print("\(document.documentID) => \(document.data())")
                }
            }
        }
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
