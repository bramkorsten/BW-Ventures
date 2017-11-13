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
    
    @IBOutlet weak var experimentNumberLabel: UILabel!
    @IBOutlet weak var experimentNumber: UIView!
    @IBOutlet weak var experimentOptions: UIImageView?
    @IBOutlet weak var experimentTitle: UILabel!
    @IBOutlet weak var experimentDescription: UILabel!
    @IBOutlet weak var experimentResults: UILabel!
    
}

class ExperimentsClass: UIViewController, UITableViewDataSource, UITableViewDelegate {

    @IBOutlet weak var experimentsTableView: UITableView!
    
    let resultsRed = UIColor(red: 229/255, green: 92/255, blue: 92/255, alpha: 1.0)
    let resultsGreen = UIColor(red: 55/255, green: 237/255, blue: 55/255, alpha: 1.0)
    let resultsGray = UIColor(red: 191/255, green: 191/255, blue: 191/255, alpha: 1.0)
    let numberGray = UIColor(red: 146/255, green: 146/255, blue: 146/255, alpha: 1.0)
    let numberBlue = UIColor(red: 0/255, green: 151/255, blue: 237/255, alpha: 1.0)
    let cellGray = UIColor(red: 249/255, green: 249/255, blue: 251/255, alpha: 1.0)
    
    var db:Firestore!
    var experimentList = [Experiment]()

    override func viewDidLoad() {
        
        super.viewDidLoad()
        // Declare Firestore
        db = Firestore.firestore()
        
        // Fill Table View
        experimentsTableView.dataSource = self
        experimentsTableView.delegate = self
        
        //////////////////////
        getExperiments()
    }

    override func didReceiveMemoryWarning() {
    
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    
    }
    
    func getExperiments() {
        
        let userDef = UserDefaults.standard
        let ventureid = userDef.string(forKey: "ventureID")
        let docRef = db.collection("Startups").document(ventureid!).collection("Experiments").order(by: "DateCreated", descending: true)
        docRef.getDocuments() { (snapshot, error) in
            
            if error != nil {
                
                print("Error getting documents: \(error)")
                
            } else {
                
                for experiment in (snapshot!.documents) {
                    
                    let exp = Experiment()
                    exp.customerLocation = experiment.data()["CustomerLocation"] as! String?
                    exp.customerSegment = experiment.data()["CustomerSegment"] as! String?
                    exp.dateCreated = experiment.data()["DateCreated"] as! Date?
                    exp.experimentName = experiment.data()["ExperimentName"] as! String?
                    exp.experimentNumber = experiment.data()["ExperimentNumber"] as! Int?
                    exp.experimentSubtitle = experiment.data()["ExperimentSubtitle"] as! String?
                    exp.failCondition = experiment.data()["FailCondition"] as! String?
                    exp.learningGoal = experiment.data()["LearningGoal"] as! String?
                    exp.numberInterviews = experiment.data()["NumberInterviews"] as! Int?
                    exp.problemHypothesis = experiment.data()["ProblemHypothesis"] as! String?
                    exp.stopCondition = experiment.data()["StopCondition"] as! String?
                    
                    self.experimentList.append(exp)
                    
                    DispatchQueue.main.async {
                        self.experimentsTableView.reloadData()
                    }
                    
                }
            }
        }
        
    }
    
    // MARK: - Table View
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return experimentList.count
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print(experimentList[indexPath.row])
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        // Create cell
        let cell = tableView.dequeueReusableCell(withIdentifier: "ExperimentCellID", for: indexPath) as! ExperimentCell
        
        cell.selectionStyle = .none
        cell.experimentNumber.layer.cornerRadius = 25
        let number = experimentList[indexPath.row].experimentNumber
        cell.experimentNumberLabel.text = String(number!)
        cell.experimentTitle.text = experimentList[indexPath.row].experimentName
        cell.experimentDescription.text = experimentList[indexPath.row].experimentSubtitle
        cell.experimentResults.text = "negative results"
        
        // Change NUMBER backgroundcolor
        if experimentList.count == experimentList[indexPath.row].experimentNumber {
            cell.experimentNumber.backgroundColor = numberBlue
        }
        
        // Change RESULTS color
        if cell.experimentResults.text == "negative results" {
            cell.experimentResults.textColor = resultsRed
        } else if cell.experimentResults.text == "positive results" {
            cell.experimentResults.textColor = resultsGreen
        } else {
            cell.experimentResults.textColor = resultsGray
        }
        
//        for item in experimentList {
//            cell.experimentTitle.text = item.experimentName
//            cell.experimentDescription.text = item.experimentSubtitle
//            cell.experimentResults.text = "Results"
//        }
        
//        DispatchQueue.main.async {
//            self.experimentsTableView.reloadData()
//        }
        
        return cell
    
    }
}
