//
//  ExperimentsClass.swift
//  ventures
//
//  Created by Fhict on 31/10/2017.
//  Copyright © 2017 Restart. All rights reserved.
//

import UIKit
import Firebase

class ExperimentCell: UITableViewCell {
    
    @IBOutlet weak var experimentNumber: UIImageView?
    @IBOutlet weak var experimentOptions: UIImageView?
    @IBOutlet weak var experimentTitle: UILabel!
    @IBOutlet weak var experimentDescription: UILabel!
    @IBOutlet weak var experimentResults: UILabel!
    
}

class ExperimentsClass: UIViewController, UITableViewDataSource, UITableViewDelegate {

    @IBOutlet weak var experimentsTableView: UITableView!
    
    var db:Firestore!
    var experimentCount = 1
    var experimentList = [Experiment]()

    override func viewDidLoad() {
        
        super.viewDidLoad()
        // Declare Firestore
        db = Firestore.firestore()
        
        // Fill Table View
        experimentsTableView.dataSource = self
        experimentsTableView.delegate = self
        
        //////////////////////
        collectExperiments()
    
    }

    override func didReceiveMemoryWarning() {
    
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    
    }
    
    func collectExperiments() {
    
        let userDef = UserDefaults.standard
        let ventureid = userDef.string(forKey: "ventureid")
        let docRef = db.collection("Startups").document(ventureid!).collection("Experiments")
        
        docRef.getDocuments() { (snapshot, error) in
            if error != nil {
            
                print("Error getting documents: \(error)")
            
            } else {
            
                for experiment in (snapshot?.documents)! {
                    
//                    let str = experiment.data()["ExperimentNumber"] as? Int
//                    let obj = experiment.data()
//                    print("\(str)")
//                    print(obj)
//                    self.experimentCount += 1
                    print(experiment.data())

                    let exp = Experiment()
                    exp.setValuesForKeys(experiment.data())
                    self.experimentList.append(exp)
                    self.experimentsTableView.reloadData()
                    
                }
            }
        }
    }
    
    // MARK: - Table View
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
     
        return experimentList.count
    
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        // Create cell
        let cell = tableView.dequeueReusableCell(withIdentifier: "ExperimentCellID", for: indexPath) as! ExperimentCell
        
        cell.experimentTitle.text = "null"
        cell.experimentDescription.text = "null"
        cell.experimentResults.text = "null"
        
        
//        // Fill cell with dummy data
//        cell.experimentTitle.text = "Hello"
//        cell.experimentDescription.text = "This is a sample text."
//        cell.experimentResults.text = "Negative results"
        
        return cell
    
    }
}
