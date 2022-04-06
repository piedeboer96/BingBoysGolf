package com.project_1_2.group_16.models;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Tree {

    public Model model;
    
    public ModelInstance instance;

    public Vector3 pos;

    public float r;

    public Tree(Model model, Vector3 pos, float r) {
        this.model = model;
        this.instance = new ModelInstance(model);
        this.pos = new Vector3(pos.x, pos.y, pos.z);
        this.r = r;

        this.instance.transform.translate(pos.x, pos.y, pos.z);
		this.instance.transform.rotate(Vector3.Y, (float)(Math.random()*360));
		this.instance.transform.scale(0.5f * r, 0.5f * r, 0.5f * r);
    }
}
