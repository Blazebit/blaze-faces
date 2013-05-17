package com.blazebit.blazefaces.examples.view;

import java.io.Serializable;

import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.blazebit.blazefaces.event.ColumnResizeEvent;
import com.blazebit.blazefaces.event.NodeCollapseEvent;
import com.blazebit.blazefaces.event.NodeExpandEvent;
import com.blazebit.blazefaces.event.NodeSelectEvent;
import com.blazebit.blazefaces.event.NodeUnselectEvent;
import com.blazebit.blazefaces.examples.domain.Document;
import com.blazebit.blazefaces.model.DefaultTreeNode;
import com.blazebit.blazefaces.model.TreeNode;

@Named
@ConversationScoped
public class DocumentsController implements Serializable {

	private static final long serialVersionUID = 1L;

	private TreeNode root;

	private TreeNode selectedNode;

	private TreeNode[] selectedNodes;

	private Document selectedDocument;

	@SuppressWarnings("unused")
	public DocumentsController() {
		root = new DefaultTreeNode("root", null);

		TreeNode documents = new DefaultTreeNode(new Document("Documents", "-",
				"Folder"), root);
		TreeNode pictures = new DefaultTreeNode(new Document("Pictures", "-",
				"Folder"), root);
		TreeNode movies = new DefaultTreeNode(new Document("Movies", "-",
				"Folder"), root);

		TreeNode work = new DefaultTreeNode(
				new Document("Work", "-", "Folder"), documents);
		TreeNode blazefaces = new DefaultTreeNode(new Document("BlazeFaces",
				"-", "Folder"), documents);

		// Documents
		TreeNode expenses = new DefaultTreeNode("document", new Document(
				"Expenses.doc", "30 KB", "Word Document"), work);
		TreeNode resume = new DefaultTreeNode("document", new Document(
				"Resume.doc", "10 KB", "Word Document"), work);
		TreeNode refdoc = new DefaultTreeNode("document", new Document(
				"RefDoc.pages", "40 KB", "Pages Document"), blazefaces);

		// Pictures
		TreeNode barca = new DefaultTreeNode("picture", new Document(
				"barcelona.jpg", "30 KB", "JPEG Image"), pictures);
		TreeNode blazeLogo = new DefaultTreeNode("picture", new Document(
				"logo.jpg", "45 KB", "JPEG Image"), pictures);
		TreeNode optimus = new DefaultTreeNode("picture", new Document(
				"blaze_logo.png", "96 KB", "PNG Image"), pictures);

		// Movies
		TreeNode pacino = new DefaultTreeNode(new Document("Al Pacino", "-",
				"Folder"), movies);
		TreeNode deniro = new DefaultTreeNode(new Document("Robert De Niro",
				"-", "Folder"), movies);

		TreeNode scarface = new DefaultTreeNode("mp3", new Document("Scarface",
				"15 GB", "Movie File"), pacino);
		TreeNode carlitosWay = new DefaultTreeNode("mp3", new Document(
				"Carlitos' Way", "24 GB", "Movie File"), pacino);

		TreeNode goodfellas = new DefaultTreeNode("mp3", new Document(
				"Goodfellas", "23 GB", "Movie File"), deniro);
		TreeNode untouchables = new DefaultTreeNode("mp3", new Document(
				"Untouchables", "17 GB", "Movie File"), deniro);
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public Document getSelectedDocument() {
		return selectedDocument;
	}

	public void setSelectedDocument(Document selectedDocument) {
		this.selectedDocument = selectedDocument;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public void onNodeExpand(NodeExpandEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Expanded", event.getTreeNode().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void onNodeCollapse(NodeCollapseEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Collapsed", event.getTreeNode().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void onNodeSelect(NodeSelectEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Selected", event.getTreeNode().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void onNodeUnselect(NodeUnselectEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Unselected", event.getTreeNode().toString());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void onResize(ColumnResizeEvent event) {
//		FacesMessage msg = new FacesMessage("Column "
//				+ event.getColumn().getId() + " resized", "W:"
//				+ event.getWidth() + ", H:" + event.getHeight());
//
//		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void deleteNode() {
		selectedNode.getChildren().clear();
		selectedNode.getParent().getChildren().remove(selectedNode);
		selectedNode.setParent(null);

		selectedNode = null;
	}
}